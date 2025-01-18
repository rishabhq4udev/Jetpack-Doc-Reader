package com.example.jetpackpdfreader.utils

import android.provider.MediaStore

 val PROJECTION = arrayOf(
    MediaStore.Files.FileColumns._ID,
    MediaStore.Files.FileColumns.TITLE,
    MediaStore.Files.FileColumns.DATA,
    MediaStore.Files.FileColumns.MIME_TYPE,
    MediaStore.Files.FileColumns.DATE_ADDED,
    MediaStore.Files.FileColumns.DATE_MODIFIED,
    MediaStore.Files.FileColumns.SIZE
)

const val ALL_FILES = "All Files"
const val ALL = "All"
const val MY_PDF = "Created"
const val PDF = "PDF"
const val DOC = "DOC"
const val PPT = "PPT"
const val EXCEL = "Excel"
const val CSV = "CSV"
const val XML = "XML"
const val TXT = "TXT"
const val IMG = "IMG"
const val LOCAL_BACKUP = "Backup"