package rpt.tool.waterdiary.utils.log

private val defaultTag: String
    get() {
        var tag = "NoDefaultTag"
        runCatching {
            tag = Throwable().stackTrace[2].className.split('.').last()
        }

        return tag
    }

fun d(logMessage: String, logTag: String = defaultTag) {
    Log.logDebug(logMessage, logTag)
}

fun w(logMessage: String, logTag: String = defaultTag) {
    Log.logWarning(logMessage, logTag)
}

fun w(throwable: Throwable, logTag: String = defaultTag) {
    Log.logWarning(throwable.localizedMessage, logTag)
}

fun e(logThrowable: Throwable, logTag: String = defaultTag) {
    Log.logThrowable(logThrowable, logTag)
}

fun i(logMessage: String, logTag: String = defaultTag) {
    Log.logInfo(logMessage, logTag)
}

fun v(logMessage: String, logTag: String = defaultTag) {
    Log.logVerbose(logMessage, logTag)
}