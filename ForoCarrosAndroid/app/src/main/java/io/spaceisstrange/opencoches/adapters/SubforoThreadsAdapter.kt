package io.spaceisstrange.opencoches.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.adapters.holders.SubforoThreadHolder
import io.spaceisstrange.opencoches.api.model.SubforoThread
import kotlinx.android.synthetic.main.list_item_subforo.view.*

/**
 * This file is part of Nowbe for Android
 *
 * Copyright (c) 2016 The Nowbe Team
 * Maintained by Fran González <@spaceisstrange>
 */

class SubforoThreadsAdapter : RecyclerView.Adapter<SubforoThreadHolder>() {
    /**
     * Lista con los hilos del subforo
     */
    var subforoThreads: MutableList<SubforoThread> = mutableListOf()

    /**
     * Método a llamar cuando el usuario hace click en un hilo
     */
    var onClick: ((thread: SubforoThread) -> Unit)? = null

    /**
     * Actualiza la lista de hilos y notifica al Adapter sobre los cambios
     */
    fun updateThreads(threads: MutableList<SubforoThread>) {
        subforoThreads.clear()
        subforoThreads.addAll(threads)
        notifyDataSetChanged()
    }

    /**
     * Añade una nueva lista de hilos filtrados y notifica al adapter
     */
    fun addThreads(threads: MutableList<SubforoThread>, filter: (thread: SubforoThread) -> Boolean) {
        val filteredThreads = threads.filter(filter)
        subforoThreads.addAll(filteredThreads)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SubforoThreadHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_subforo_thread, parent, false)
        val holder = SubforoThreadHolder(view)

        // Invocamos el onClick (si hay alguno definido) con el hilo que el usuario ha elegido
        holder.itemView.llClickable.setOnClickListener {
            onClick?.invoke(subforoThreads[holder.adapterPosition])
        }

        return holder
    }

    override fun onBindViewHolder(holder: SubforoThreadHolder?, position: Int) {
        holder?.bindView(subforoThreads[position])
    }

    override fun getItemCount(): Int {
        return subforoThreads.size
    }
}