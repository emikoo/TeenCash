package com.teenteen.teencash.presentation.ui.fragments.main.history

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.MPPointF
import com.teenteen.teencash.R
import com.teenteen.teencash.data.model.History
import com.teenteen.teencash.databinding.FragmentHistoryBinding
import com.teenteen.teencash.presentation.base.BaseFragment
import com.teenteen.teencash.presentation.extensions.getCurrentDate
import com.teenteen.teencash.presentation.extensions.getCurrentMonth
import com.teenteen.teencash.presentation.extensions.getCurrentWeek
import com.teenteen.teencash.presentation.utills.internetIsConnected
import com.teenteen.teencash.view_model.HistoryViewModel

class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {

    var historyArray = mutableListOf<History>()
    lateinit var adapter: HistoryAdapter
    lateinit var viewModel: HistoryViewModel
    val entries: ArrayList<PieEntry> = ArrayList()

    override fun setupViews() {
        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        progressDialog.show()
        viewModel.getHistoryToday(prefs.getCurrentUserId(), getCurrentDate())
        setupRecyclerView()
        setupSpinner()
    }

    private fun setupRecyclerView() {
        adapter = HistoryAdapter(historyArray)
        binding.recyclerView.adapter = adapter
    }

    private fun setupSpinner() {
        val adapter =
            ArrayAdapter.createFromResource(requireActivity(), R.array.spinner_date , R.layout.spinner_history)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_history)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>? , p1: View? , p2: Int , p3: Long) {
                progressDialog.show()
                when (binding.spinner.selectedItemPosition) {
                    0 -> viewModel.getHistory(prefs.getCurrentUserId())
                    1 -> viewModel.getHistoryToday(prefs.getCurrentUserId(), getCurrentDate())
                    2 -> viewModel.getHistoryByRange(prefs.getCurrentUserId(), getCurrentWeek())
                    3 -> viewModel.getHistoryByMonth(prefs.getCurrentUserId(), getCurrentMonth())
                    else -> viewModel.getHistory(prefs.getCurrentUserId())
                }
            }
        }
    }

    private fun setupPieChart() {
        val pieChart = binding.pieChart
        pieChart.setUsePercentValues(true)
        pieChart.animateY(1000, Easing.EaseInOutQuad)
        pieChart.dragDecelerationFrictionCoef = 0.95f
        pieChart.isDrawHoleEnabled = false
        pieChart.isHighlightPerTapEnabled = true
        pieChart.setDrawCenterText(true)
        pieChart.isRotationEnabled = true
        pieChart.rotationAngle = 0f
        pieChart.description.isEnabled = false
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(15f)

        val l: Legend = pieChart.legend
        l.isEnabled = true
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.textColor = resources.getColor(R.color.white)

        val dataSet = PieDataSet(entries, "")

        val colors: ArrayList<Int> = ArrayList<Int>().apply {
            add(resources.getColor(R.color.pie_red))
            add(resources.getColor(R.color.pie_yellow))
            add(resources.getColor(R.color.pie_green))
            add(resources.getColor(R.color.pie_dark_green))
            add(resources.getColor(R.color.pie_midnight_green))
            add(resources.getColor(R.color.pie_dark_blue))
            add(resources.getColor(R.color.pie_blue))
            add(resources.getColor(R.color.pie_purple))
            add(resources.getColor(R.color.pie_mulberry))
            add(resources.getColor(R.color.pie_pink))
            add(resources.getColor(R.color.red))
            add(resources.getColor(R.color.orange))
            add(resources.getColor(R.color.green))
            add(resources.getColor(R.color.black))
        }
        dataSet.colors = colors
        val data = PieData(dataSet)
        data.setValueTextColor(android.R.color.transparent)
        binding.pieChart.data = data
        binding.pieChart.highlightValues(null)
        binding.pieChart.invalidate()
    }

    override fun subscribeToLiveData() {
        if (internetIsConnected(requireContext())) {
            viewModel.history.observe(viewLifecycleOwner, Observer { it ->
                historyArray.clear()
                historyArray.addAll(it.sortedByDescending { it.time })
                entries.clear()
                val spentHistory = historyArray.filter { it.spent }
                val groupBy = spentHistory.sortedWith(compareBy(History::name, History::image))
                    .groupBy { it.name }
                var amount = 0
                var name = ""
                val oneGroup = mutableListOf<History>()
                for (i in groupBy){
                    for (spentItem in spentHistory) {
                        if (spentItem.name == i.key) oneGroup.add(spentItem)
                    }
                    for (b in oneGroup) {
                        amount += b.amount
                        name = b.name
                    }
                    entries.add(PieEntry(amount.toFloat(), name))
                    amount = 0
                    oneGroup.clear()
                }
                adapter.notifyDataSetChanged()
                setupPieChart()
                progressDialog.dismiss()
            })
        } else Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
    }

    override fun attachBinding(
        list: MutableList<FragmentHistoryBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(FragmentHistoryBinding.inflate(layoutInflater , container , attachToRoot))
    }
}