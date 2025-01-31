package rpt.tool.waterdiary.utils.file

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.DatabaseUtils
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import rpt.tool.waterdiary.utils.log.d
import rpt.tool.waterdiary.utils.log.e
import java.io.File
import java.io.FileFilter
import java.text.DecimalFormat
import java.util.Locale

class FileUtils private constructor() //private constructor to enforce Singleton pattern
{
    fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }

    companion object {
        const val TAG: String = "FileUtils"
        private const val DEBUG = false // Set to true to enable logging

        const val MIME_TYPE_AUDIO: String = "audio/*"
        const val MIME_TYPE_TEXT: String = "text/*"
        const val MIME_TYPE_IMAGE: String = "image/*"
        const val MIME_TYPE_VIDEO: String = "video/*"
        const val MIME_TYPE_APP: String = "application/*"

        const val HIDDEN_PREFIX: String = "."

        private fun getExtension(uri: String?): String? {
            if (uri == null) {
                return null
            }

            val dot = uri.lastIndexOf(".")
            return if (dot >= 0) {
                uri.substring(dot)
            } else {
                ""
            }
        }

        private fun isLocal(url: String?): Boolean {
            if (url != null && !url.startsWith("http://") && !url.startsWith("https://")) {
                return true
            }
            return false
        }

        private fun isMediaUri(uri: Uri): Boolean {
            return "media".equals(uri.authority, ignoreCase = true)
        }

        private fun getUri(file: File?): Uri? {
            if (file != null) {
                return Uri.fromFile(file)
            }
            return null
        }

        fun getPathWithoutFilename(file: File?): File? {
            if (file != null) {
                if (file.isDirectory) {
                    // no file to be split off. Return everything
                    return file
                } else {
                    val filename = file.name
                    val filepath = file.absolutePath

                    // Construct path without file name.
                    var pathwithoutname = filepath.substring(
                        0,
                        filepath.length - filename.length
                    )
                    if (pathwithoutname.endsWith("/")) {
                        pathwithoutname = pathwithoutname.substring(0, pathwithoutname.length - 1)
                    }
                    return File(pathwithoutname)
                }
            }
            return null
        }

        private fun getMimeType(file: File): String? {
            val extension = getExtension(file.name)

            if (extension!!.length > 0) return MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                extension.substring(1)
            )

            return "application/octet-stream"
        }


        private fun getMimeType(context: Context, uri: Uri): String? {
            val file = getPath(context, uri)?.let { File(it) }
            return file?.let { getMimeType(it) }
        }

        private fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }


        private fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        private fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

        private fun isGooglePhotosUri(uri: Uri): Boolean {
            return "com.google.android.apps.photos.content" == uri.authority
        }

        private fun getDataColumn(
            context: Context, uri: Uri, selection: String?,
            selectionArgs: Array<String>?
        ): String? {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(
                column
            )

            try {
                cursor = context.contentResolver.query(
                    uri, projection, selection, selectionArgs,
                    null
                )
                if (cursor != null && cursor.moveToFirst()) {
                    if (DEBUG) DatabaseUtils.dumpCursor(cursor)

                    val column_index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(column_index)
                }
            } finally {
                cursor?.close()
            }
            return null
        }

        private fun getPath(context: Context, uri: Uri): String? {
            if (DEBUG) d(
                "$TAG File -",
                "Authority: " + uri.authority +
                        ", Fragment: " + uri.fragment +
                        ", Port: " + uri.port +
                        ", Query: " + uri.query +
                        ", Scheme: " + uri.scheme +
                        ", Host: " + uri.host +
                        ", Segments: " + uri.pathSegments.toString()
            )

            val isKitKat = true

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split =
                        docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }

                } else if (isDownloadsDocument(uri)) {
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), id.toLong()
                    )

                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split =
                        docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }

                    val selection = "_id=?"
                    val selectionArgs = arrayOf(
                        split[1]
                    )

                    return getDataColumn(
                        context,
                        contentUri!!, selection, selectionArgs
                    )
                }
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                // Return the remote address

                if (isGooglePhotosUri(uri)) return uri.lastPathSegment

                return getDataColumn(context, uri, null, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }

            return null
        }

        fun getFile(context: Context, uri: Uri?): File? {
            if (uri != null) {
                val path = getPath(context, uri)
                if (path != null && isLocal(path)) {
                    return File(path)
                }
            }
            return null
        }

        fun getReadableFileSize(size: Int): String {
            val BYTES_IN_KILOBYTES = 1024
            val dec = DecimalFormat("###.#")
            val KILOBYTES = " KB"
            val MEGABYTES = " MB"
            val GIGABYTES = " GB"
            var fileSize = 0f
            var suffix = KILOBYTES

            if (size > BYTES_IN_KILOBYTES) {
                fileSize = (size / BYTES_IN_KILOBYTES).toFloat()
                if (fileSize > BYTES_IN_KILOBYTES) {
                    fileSize /= BYTES_IN_KILOBYTES
                    if (fileSize > BYTES_IN_KILOBYTES) {
                        fileSize /= BYTES_IN_KILOBYTES
                        suffix = GIGABYTES
                    } else {
                        suffix = MEGABYTES
                    }
                }
            }
            return (dec.format(fileSize.toDouble()) + suffix).toString()
        }

        fun getThumbnail(context: Context, file: File): Bitmap? {
            return getThumbnail(
                context,
                getUri(file)!!, getMimeType(file)!!
            )
        }

        fun getThumbnail(context: Context, uri: Uri): Bitmap? {
            return getThumbnail(
                context, uri,
                getMimeType(context, uri)!!
            )
        }

        fun getThumbnail(context: Context, uri: Uri, mimeType: String): Bitmap? {
            if (DEBUG) d(TAG, "Attempting to get thumbnail")

            if (!isMediaUri(uri)) {
                val e = Exception("You can only retrieve thumbnails for images and videos.")
                e.message?.let { e(Throwable(e), it) }
                return null
            }

            var bm: Bitmap? = null
            val resolver = context.contentResolver
            var cursor: Cursor? = null
            try {
                cursor = resolver.query(uri, null, null, null, null)
                if (cursor!!.moveToFirst()) {
                    val id = cursor.getInt(0)
                    if (DEBUG) d(
                        TAG,
                        "Got thumb ID: $id"
                    )

                    if (mimeType.contains("video")) {
                        bm = MediaStore.Video.Thumbnails.getThumbnail(
                            resolver,
                            id.toLong(),
                            MediaStore.Video.Thumbnails.MINI_KIND,
                            null
                        )
                    } else if (mimeType.contains(MIME_TYPE_IMAGE)) {
                        bm = MediaStore.Images.Thumbnails.getThumbnail(
                            resolver,
                            id.toLong(),
                            MediaStore.Images.Thumbnails.MINI_KIND,
                            null
                        )
                    }
                }
            } catch (e: Exception) {
                if (DEBUG)
                    e.message?.let { e(Throwable(e), it) }
            } finally {
                cursor?.close()
            }
            return bm
        }

        var sComparator: Comparator<File> =
            Comparator { f1, f2 -> // Sort alphabetically by lower case, which is much cleaner
                f1.name.lowercase(Locale.getDefault()).compareTo(
                    f2.name.lowercase(Locale.getDefault())
                )
            }

        var sFileFilter: FileFilter = FileFilter { file ->
            val fileName = file.name
            // Return files only (not directories) and skip hidden files
            file.isFile && !fileName.startsWith(HIDDEN_PREFIX)
        }

        var sDirFilter: FileFilter = FileFilter { file ->
            val fileName = file.name
            // Return directories only and skip hidden directories
            file.isDirectory && !fileName.startsWith(HIDDEN_PREFIX)
        }

        fun createGetContentIntent(): Intent {
            // Implicitly allow the user to select a particular kind of data
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            // The MIME data type filter
            intent.setType("*/*")
            // Only return URIs that can be opened with ContentResolver
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            return intent
        }
    }
}

