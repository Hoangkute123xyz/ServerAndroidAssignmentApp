package com.hoangpro.serverandroidassignmentapp.helper

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

class AnimationHelper {
    companion object{
        fun scaleAnimation(view: View,scale:Float,onClick:()->Unit){
            view.setOnClickListener {
                val objectAnimatorX = ObjectAnimator.ofFloat(view,"ScaleX",scale)
                val objectAnimatorY = ObjectAnimator.ofFloat(view,"ScaleY",scale)

                val objectAnimatorXBack = ObjectAnimator.ofFloat(view,"ScaleX",1f)
                val objectAnimatorYBack = ObjectAnimator.ofFloat(view,"ScaleY",1f)

                objectAnimatorX.duration=200
                objectAnimatorY.duration=200
                objectAnimatorYBack.duration=200
                objectAnimatorXBack.duration=200

                val anSet1 = AnimatorSet()
                anSet1.play(objectAnimatorX).with(objectAnimatorY)

                val anSet2 = AnimatorSet()
                anSet2.play(objectAnimatorXBack).with(objectAnimatorYBack)

                val anSet = AnimatorSet()
                anSet.play(anSet1).before(anSet2)
                anSet.addListener(object: AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        onClick()
                    }
                })
                anSet.start()
            }
        }
    }
}