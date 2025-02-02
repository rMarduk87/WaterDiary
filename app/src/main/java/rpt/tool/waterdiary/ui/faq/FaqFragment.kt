package rpt.tool.waterdiary.ui.faq

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import rpt.tool.waterdiary.DashboardActivity
import rpt.tool.waterdiary.R
import rpt.tool.waterdiary.base.NavBaseFragment
import rpt.tool.waterdiary.data.model.FAQModel
import rpt.tool.waterdiary.databinding.FragmentFaqBinding
import rpt.tool.waterdiary.utils.view.custom.AnimationUtils


class FaqFragment : NavBaseFragment<FragmentFaqBinding>(FragmentFaqBinding::inflate) {

    var lst_faq: MutableList<FAQModel> = ArrayList()
    var answer_block_lst: MutableList<LinearLayout> = ArrayList()
    var img_faq_lst: MutableList<ImageView> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        body()
    }

    private fun body() {
        binding.include1.lblToolbarTitle.text = sh!!.get_string(R.string.str_faqs)
        binding.include1.leftIconBlock.setOnClickListener { finish() }
        binding.include1.rightIconBlock.visibility = View.GONE

        setFAQData()
        loadFAQData()
    }

    private fun finish() {
        startActivity(Intent(requireActivity(), DashboardActivity::class.java))
    }

    private fun setFAQData() {
        var faqModel = FAQModel()
        faqModel.question = sh!!.get_string(R.string.faq_question_1)
        faqModel.answer = sh!!.get_string(R.string.faq_answer_1)
        lst_faq.add(faqModel)

        faqModel = FAQModel()
        faqModel.question = sh!!.get_string(R.string.faq_question_2)
        faqModel.answer = sh!!.get_string(R.string.faq_answer_2)
        lst_faq.add(faqModel)

        faqModel = FAQModel()
        faqModel.question = sh!!.get_string(R.string.faq_question_3)
        faqModel.answer = sh!!.get_string(R.string.faq_answer_3)
        lst_faq.add(faqModel)

        faqModel = FAQModel()
        faqModel.question = sh!!.get_string(R.string.faq_question_12)
        faqModel.answer = sh!!.get_string(R.string.faq_answer_12)
        lst_faq.add(faqModel)

        faqModel = FAQModel()
        faqModel.question = sh!!.get_string(R.string.faq_question_13)
        faqModel.answer = sh!!.get_string(R.string.faq_answer_13)
        lst_faq.add(faqModel)

        faqModel = FAQModel()
        faqModel.question = sh!!.get_string(R.string.faq_question_4)
        faqModel.answer = sh!!.get_string(R.string.faq_answer_4)
        lst_faq.add(faqModel)

        faqModel = FAQModel()
        faqModel.question = sh!!.get_string(R.string.faq_question_11)
        faqModel.answer = sh!!.get_string(R.string.faq_answer_11)
        lst_faq.add(faqModel)

        faqModel = FAQModel()
        faqModel.question = sh!!.get_string(R.string.faq_question_5)
        faqModel.answer = sh!!.get_string(R.string.faq_answer_5)
        lst_faq.add(faqModel)

        faqModel = FAQModel()
        faqModel.question = sh!!.get_string(R.string.faq_question_6)
        faqModel.answer = sh!!.get_string(R.string.faq_answer_6)
        lst_faq.add(faqModel)

        faqModel = FAQModel()
        faqModel.question = sh!!.get_string(R.string.faq_question_7)
        faqModel.answer = sh!!.get_string(R.string.faq_answer_7)
        lst_faq.add(faqModel)

        faqModel = FAQModel()
        faqModel.question = sh!!.get_string(R.string.faq_question_8)
        faqModel.answer = sh!!.get_string(R.string.faq_answer_8)
        lst_faq.add(faqModel)

        faqModel = FAQModel()
        faqModel.question = sh!!.get_string(R.string.faq_question_9)
        faqModel.answer = sh!!.get_string(R.string.faq_answer_9)
        lst_faq.add(faqModel)
    }

    private fun loadFAQData() {
        binding.faqBlock.removeAllViews()
        for (k in lst_faq.indices) {
            val pos = k

            val rowModel = lst_faq[k]

            val layoutInflater = LayoutInflater.from(mContext)
            val itemView: View = layoutInflater.inflate(R.layout.row_item_faq, null, false)

            val lbl_question = itemView.findViewById<AppCompatTextView>(R.id.lbl_question)
            val lbl_answer = itemView.findViewById<AppCompatTextView>(R.id.lbl_answer)

            val question_block = itemView.findViewById<LinearLayout>(R.id.question_block)
            val answer_block = itemView.findViewById<LinearLayout>(R.id.answer_block)
            val img_faq = itemView.findViewById<ImageView>(R.id.img_faq)

            answer_block_lst.add(answer_block)
            img_faq_lst.add(img_faq)

            lbl_question.text = rowModel.question
            lbl_answer.text = rowModel.answer

            question_block.setOnClickListener {
                if (answer_block.visibility == View.GONE) {
                    viewAnswer(pos)
                    img_faq.setImageResource(R.drawable.ic_faq_minus)
                    AnimationUtils.expand(answer_block)
                } else {
                    img_faq.setImageResource(R.drawable.ic_faq_plus)
                    AnimationUtils.collapse(answer_block)
                }
            }

            binding.faqBlock.addView(itemView)
        }
    }

    fun viewAnswer(pos: Int) {
        for (k in answer_block_lst.indices) {
            if (k == pos) continue
            else {
                img_faq_lst[k].setImageResource(R.drawable.ic_faq_plus)
                AnimationUtils.collapse(answer_block_lst[k])
            }
        }
    }
}