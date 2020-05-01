package com.hoangpro.serverandroidassignmentapp.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.hoangpro.serverandroidassignmentapp.R
import com.hoangpro.serverandroidassignmentapp.helper.AnimationHelper

class NumberView(context: Context?, attrs: AttributeSet?) : ConstraintLayout(context, attrs){
    //view
    private lateinit var imgLeft:ImageView
    private lateinit var imgRight:ImageView
    private lateinit var tvResult:TextView

    private var number = 1
    var max =0
    var onNumberChange:((newNumber:Int,oldNumber:Int)->Unit)?=null
    init {
        View.inflate(context, R.layout.layout_number_view,this)
        setupView()
    }

    private fun setupView() {
        imgLeft = findViewById(R.id.imgLeft)
        imgRight = findViewById(R.id.imgRight)
        tvResult = findViewById(R.id.tvResult)

        imgLeft.setOnClickListener{
            if (number>1){
                number--
                tvResult.text= number.toString()
                if (onNumberChange!=null){
                    onNumberChange!!(number,number+1)
                }
            }
        }
        imgRight.setOnClickListener{
            if (max ==0 || number<max){
                number++
                tvResult.text=number.toString()
                if (onNumberChange!=null){
                    onNumberChange!!(number,number-1)
                }
            }
        }

    }

    fun setNumber(number:Int){
        this.number=number
    }
    fun getNumber():Int=number
}