package rpt.tool.waterdiary.data.model

class LanguageModel {
    var name: String = ""
    var code: String = ""
    var title: String = ""
    var isSelected: Boolean = false

    fun isSelected(isSelected: Boolean) {
        this.isSelected = isSelected
    }
}
