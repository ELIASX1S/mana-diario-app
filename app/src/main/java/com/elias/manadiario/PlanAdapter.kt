package com.elias.manadiario

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlanAdapter(
    private val plans: List<DevotionalPlan>,
    private val onPlanClick: (DevotionalPlan) -> Unit
) : RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {

    class PlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.planTitleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.planDescriptionTextView)
        val durationTextView: TextView = itemView.findViewById(R.id.planDurationTextView)
        val badgeTextView: TextView = itemView.findViewById(R.id.planBadgeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plan, parent, false)
        return PlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val plan = plans[position]

        holder.titleTextView.text = plan.nome
        holder.descriptionTextView.text = plan.descricao
        holder.durationTextView.text = "${plan.dias.size} dias"
        holder.badgeTextView.text = getPlanBadge(plan)

        holder.itemView.setOnClickListener {
            onPlanClick(plan)
        }
    }

    override fun getItemCount(): Int = plans.size

    private fun getPlanBadge(plan: DevotionalPlan): String {
        return when (plan.dias.size) {
            7 -> "Iniciante"
            21 -> "Constância"
            30 -> "Profundo"
            else -> "Plano"
        }
    }
}