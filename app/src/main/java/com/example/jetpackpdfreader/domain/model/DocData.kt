package com.example.jetpackpdfreader.domain.model

import com.example.jetpackpdfreader.utils.ALL
import com.example.jetpackpdfreader.utils.CSV
import com.example.jetpackpdfreader.utils.DOC
import com.example.jetpackpdfreader.utils.EXCEL
import com.example.jetpackpdfreader.utils.PDF
import com.example.jetpackpdfreader.utils.PPT
import com.example.jetpackpdfreader.utils.TXT
import com.example.jetpackpdfreader.utils.XML


data class DocData(
    val id: Long,
    val title: String,
    val mediaPath: String,
    val mimeType: String,
    val dateAdded: Long,
    val dateModified: Long,
    val size: Long,
) {
    fun getFileType(): String {
        return when {
            mimeType.contains("pdf") || mediaPath.endsWith(".pdf", ignoreCase = true) -> PDF
            mimeType.contains("msword") || mediaPath.endsWith(".doc", ignoreCase = true) ||
                    mediaPath.endsWith(".docx", ignoreCase = true) -> DOC

            mimeType.contains("ms-powerpoint") || mediaPath.endsWith(".ppt", ignoreCase = true) ||
                    mediaPath.endsWith(".pptx", ignoreCase = true) -> PPT

            mimeType.contains("ms-excel") || mediaPath.endsWith(".xls", ignoreCase = true) ||
                    mediaPath.endsWith(".xlsx", ignoreCase = true) -> EXCEL

            mimeType.contains("text/csv") || mediaPath.endsWith(".csv", ignoreCase = true) -> CSV
            mimeType.contains("text/xml") || mediaPath.endsWith(".xml", ignoreCase = true) -> XML
            mimeType.contains("text/plain") || mediaPath.endsWith(".txt", ignoreCase = true) -> TXT
            else -> ALL
        }
    }
}
