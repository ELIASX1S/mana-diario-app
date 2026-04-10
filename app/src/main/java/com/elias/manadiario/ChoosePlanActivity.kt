package com.elias.manadiario

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChoosePlanActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlanAdapter
    private lateinit var journeyManager: JourneyManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_plan)

        journeyManager = JourneyManager(this)

        val toolbar: Toolbar = findViewById(R.id.choosePlanToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Escolher Plano"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.plansRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val plans = DevotionalRepository.getAllPlans()

        adapter = PlanAdapter(plans) { selectedPlan ->
            journeyManager.startPlan(selectedPlan.id)

            val intent = Intent(this, JourneyActivity::class.java)
            startActivity(intent)
            finish()
        }

        recyclerView.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}