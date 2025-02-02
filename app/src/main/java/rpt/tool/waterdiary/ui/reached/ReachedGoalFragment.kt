package rpt.tool.waterdiary.ui.reached

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import rpt.tool.waterdiary.AppUtils
import rpt.tool.waterdiary.DashboardActivity
import rpt.tool.waterdiary.R
import rpt.tool.waterdiary.base.NavBaseFragment
import rpt.tool.waterdiary.data.model.ReachedGoal
import rpt.tool.waterdiary.databinding.FragmentReachedGoalBinding
import rpt.tool.waterdiary.utils.log.i
import rpt.tool.waterdiary.utils.view.adapters.ReachedGoalAdapter

class ReachedGoalFragment : NavBaseFragment<FragmentReachedGoalBinding>(FragmentReachedGoalBinding::inflate) {

    var reachedGoals: ArrayList<ReachedGoal> = ArrayList()
    var adapter: ReachedGoalAdapter? = null
    var isLoading: Boolean = true
    var perPage: Int = 20
    var page: Int = 0
    var beforeLoad: Int = 0
    var afterLoad: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        body()
    }

    private fun body() {
        binding.include1.lblToolbarTitle.text = sh!!.get_string(R.string.str_reached)
        binding.include1.leftIconBlock.setOnClickListener{ finish() }
        binding.include1.rightIconBlock.visibility = View.GONE

        binding.reachedGoalRecyclerView.isNestedScrollingEnabled = false

        adapter = ReachedGoalAdapter(act!!, reachedGoals, object : ReachedGoalAdapter.CallBack {

            override fun onClickSelect(reachedGoal: ReachedGoal, position: Int) {
            }
        })

        binding.reachedGoalRecyclerView.setLayoutManager(
            LinearLayoutManager(
                act,
                LinearLayoutManager.VERTICAL,
                false
            )
        )

        binding.reachedGoalRecyclerView.setAdapter(adapter)

        load_reachedGoal(false)

        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener {
                v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val TAG = "nested_sync"
            if (scrollY > oldScrollY) {
                i(TAG, "Scroll DOWN")
            }
            if (scrollY < oldScrollY) {
                i(TAG, "Scroll UP")
            }

            if (scrollY == 0) {
                i(TAG, "TOP SCROLL")
            }
            if (scrollY == (v.getChildAt(0).measuredHeight - v.measuredHeight)) {
                i(TAG, "BOTTOM SCROLL")

                if (isLoading) {
                    isLoading = false

                    page++

                    load_reachedGoal(true)
                }
            }
        })
    }

    private fun finish() {
        startActivity(Intent(requireActivity(), DashboardActivity::class.java))
    }

    @SuppressLint("Recycle", "NotifyDataSetChanged")
    fun load_reachedGoal(closeLoader: Boolean) {

        val start_idx = page * perPage

        val query =
            "SELECT * FROM tbl_reached_goal_details ORDER BY DrinkDate DESC limit $start_idx,$perPage"

        val c: Cursor = AppUtils.SDB!!.rawQuery(query, null)

        val arr_data = ArrayList<HashMap<String, String>>()

        if (c.moveToFirst()) {
            do {
                val map = HashMap<String, String>()
                for (i in 0 until c.columnCount) {
                    map[c.getColumnName(i)] = c.getString(i) ?:""
                }

                arr_data.add(map)
            } while (c.moveToNext())
        }

        for (k in arr_data.indices) {
            val reachedGoal = ReachedGoal()

            reachedGoal.containerValue = "" + (arr_data[k]["ContainerValue"]!!.toDouble())
            reachedGoal.containerValueOZ = "" + (arr_data[k]["ContainerValueOZ"]!!.toDouble())
            reachedGoal.date = arr_data[k]["DrinkDate"]

            reachedGoals.add(reachedGoal)
        }

        afterLoad = reachedGoals.size

        isLoading = if (afterLoad == 0) false
        else if (afterLoad > beforeLoad) true
        else false

        if (reachedGoals.size > 0)
            binding.lblNoRecordFound.visibility = View.GONE
        else binding.lblNoRecordFound.visibility = View.VISIBLE

        adapter!!.notifyDataSetChanged()
    }
}