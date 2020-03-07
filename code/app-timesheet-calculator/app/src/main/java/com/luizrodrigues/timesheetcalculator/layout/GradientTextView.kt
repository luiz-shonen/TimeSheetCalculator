package com.luizrodrigues.timesheetcalculator.layout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.luizrodrigues.timesheetcalculator.R

/**
 * Created by Luiz Rodrigues on 09/02/2020.
 */
class GradientTextView : AppCompatTextView {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        //Setting the gradient if layout is changed
        if (changed) {
            paint.shader = LinearGradient(
                0F,
                0F,
                width.toFloat(),
                height.toFloat(),
                intArrayOf(
                    ContextCompat.getColor(
                        context,
                        R.color.colorPrimaryDark
                    ),
                    ContextCompat.getColor(
                        context,
                        R.color.colorAccent
                    ),
                    ContextCompat.getColor(
                        context,
                        R.color.colorPrimary
                    )
                ),
                null,
                Shader.TileMode.CLAMP
            )
        }
    }
}