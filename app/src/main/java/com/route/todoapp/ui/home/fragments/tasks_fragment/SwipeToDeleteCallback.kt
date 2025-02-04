package com.route.todoapp.ui.home.fragments.tasks_fragment

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.route.todoapp.R

abstract class SwipeToDeleteCallback(context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val deleteIcon: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_delete)!!
    private val background: ColorDrawable = ColorDrawable(Color.RED)

    override fun onMove(
        recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onItemSwiped(viewHolder.adapterPosition)
    }

    abstract fun onItemSwiped(position: Int)

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2
        val iconTop = itemView.top + iconMargin
        val iconBottom = iconTop + deleteIcon.intrinsicHeight
        val iconLeft = itemView.right - iconMargin - deleteIcon.intrinsicWidth
        val iconRight = itemView.right - iconMargin

        background.setBounds(itemView.left, itemView.top, itemView.right, itemView.bottom)
        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

        background.draw(c)
        deleteIcon.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
