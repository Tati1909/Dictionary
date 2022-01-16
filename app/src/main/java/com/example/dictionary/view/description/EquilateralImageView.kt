package com.example.dictionary.view.description

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class EquilateralImageView : AppCompatImageView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
        super(context, attrs, defStyleAttr)

    /**Переопределяем onMeasure и передаём в него ширину изображения в качестве
     * ширины и высоты. Так мы получим аккуратное квадратное изображение
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}