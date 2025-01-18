package com.example.jetpackpdfreader.data.local.repository

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import com.example.jetpackpdfreader.di.CoroutineDispatcherProvider
import com.example.jetpackpdfreader.domain.model.DocData
import com.example.jetpackpdfreader.domain.model.FetchStatus
import com.example.jetpackpdfreader.utils.ALL
import com.example.jetpackpdfreader.utils.CSV
import com.example.jetpackpdfreader.utils.DOC
import com.example.jetpackpdfreader.utils.EXCEL
import com.example.jetpackpdfreader.utils.PDF
import com.example.jetpackpdfreader.utils.PPT
import com.example.jetpackpdfreader.utils.PROJECTION
import com.example.jetpackpdfreader.utils.TXT
import com.example.jetpackpdfreader.utils.XML
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DocRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context, private val dispatcher: CoroutineDispatcherProvider,
) : DocRepository {
    private val _fetchingStatus = MutableStateFlow<FetchStatus>(FetchStatus.Idle)

    private val docCollections = mutableMapOf<String, MutableStateFlow<List<DocData>>>()

    override fun fetchFiles(fileType: String): Flow<List<DocData>> = flow {
        _fetchingStatus.value = FetchStatus.Fetching

        try {
            val files = queryFiles(fileType)
            updateCollections(fileType, files)
            emit(files)
            _fetchingStatus.value = FetchStatus.Completed
        } catch (e: Exception) {
            _fetchingStatus.value = FetchStatus.Error(e.message ?: "Unknown error")
            throw e
        }
    }.flowOn(dispatcher.io)

    override val fetchingStatus: StateFlow<FetchStatus>
        get() = _fetchingStatus.asStateFlow()

    private suspend fun queryFiles(fileType: String): List<DocData> {
        val mimeTypes = getMimeTypeArray(fileType)
        val selection = mimeTypes.joinToString(" OR ") {
            "${MediaStore.Files.FileColumns.MIME_TYPE} = ?"
        }

        return withContext(dispatcher.io) {
            val files = mutableListOf<DocData>()

            context.contentResolver.query(
                MediaStore.Files.getContentUri("external"),
                PROJECTION,
                selection,
                mimeTypes,
                "${MediaStore.Files.FileColumns.DISPLAY_NAME} DESC"
            )?.use { cursor ->
                while (cursor.moveToNext()) {
                    getMediaDataFromCursor(cursor)?.let {
                        if (!it.mediaPath.contains(".ALlDocReader")) {
                            files.add(it)
                        }
                    }
                }
            }
            files
        }
    }

    private fun getMediaDataFromCursor(cursor: Cursor): DocData? {
        return try {
            DocData(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)),
                title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE)),
                mediaPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)),
                mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)),
                dateAdded = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)),
                dateModified = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)),
                size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)),
            )
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun updateCollections(fileType: String, files: List<DocData>) {
        if (fileType == ALL) {
            files.groupBy { it.getFileType() }.forEach { (type, typeFiles) ->
                docCollections[type]?.emit(typeFiles)
            }
        } else {
            docCollections[fileType]?.emit(files)
        }
    }

    private fun getMimeTypeArray(fileType: String): Array<String> {
        return when (fileType) {
            ALL -> arrayOf(
                "application/pdf",
                "application/vnd.ms-powerpoint",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "text/plain",
                "text/xml",
                "text/csv",
                "text/comma-separated-values"
            )

            PDF -> arrayOf("application/pdf")
            DOC -> arrayOf(
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            )

            PPT -> arrayOf(
                "application/vnd.ms-powerpoint",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation"
            )

            EXCEL -> arrayOf(
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            )

            CSV -> arrayOf("text/csv", "text/comma-separated-values")
            XML -> arrayOf("text/xml")
            TXT -> arrayOf("text/plain")
            else -> {
                arrayOf(
                    "application/pdf",
                    "application/vnd.ms-powerpoint",
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                    "application/msword",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                    "application/vnd.ms-excel",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    "text/plain",
                    "text/xml",
                    "text/csv",
                    "text/comma-separated-values"
                )
            }
        }
    }

}