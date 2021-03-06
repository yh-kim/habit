/*
 * Copyright 2017 Yonghoon Kim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pickth.habit.view.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.pickth.habit.R
import com.pickth.habit.view.main.adapter.item.Habit
import kotlinx.android.synthetic.main.dialog_add_habit.*
import org.jetbrains.anko.alert
import java.util.*
import com.pes.androidmaterialcolorpickerdialog.ColorPicker
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback


/**
 * Created by yonghoon on 2017-08-14
 */

class AddHabitDialog(context: Context, val listener: View.OnClickListener, val test: String, val habit: Habit?) : Dialog(context, R.style.AppTheme_NoTitle_Translucent) {
    constructor(context: Context, listener: View.OnClickListener) : this(context, listener, "", null)

    var itemColor = habit?.color ?: ContextCompat.getColor(context, R.color.colorMainAccent)
//    val colorPicker = ColorPicker(
//            context as Activity, // Context
////            255, // Default Alpha value
//            127, // Default Red value
//            123, // Default Green value
//            67 // Default Blue value
//    )
    val colorPicker = ColorPicker(
            context as Activity, // Context
//            255, // Default Alpha value
            Color.red(itemColor),
            Color.green(itemColor),
            Color.blue(itemColor)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.run {
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
            setGravity(Gravity.CENTER)
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }

        setContentView(R.layout.dialog_add_habit)
        btn_add_habit_submit.setOnClickListener(listener)
        et_add_habit_title.setText(test)
        ll_add_habit_back.setBackgroundColor(itemColor)

        var editTitle = et_add_habit_title.background.apply {
            setColorFilter(
                    ContextCompat.getColor(context, R.color.colorWhite),
                    PorterDuff.Mode.SRC_ATOP
            )
        }
        if (Build.VERSION.SDK_INT > 16) et_add_habit_title.background = editTitle
        else et_add_habit_title.setBackgroundDrawable(editTitle)

        colorPicker.setCallback(ColorPickerCallback { color ->
            ll_add_habit_back.setBackgroundColor(color)
            itemColor = color
            colorPicker.hide()
        })

        btn_select_color.setOnClickListener {
            colorPicker.show()
        }

        btn_add_habit_cancel.setOnClickListener {
            dismiss()
        }
    }

    fun addHabit(): Habit? {
        var title = et_add_habit_title.text.toString()
        if (title == "") {
            // 제목을 안지었을 때
            context.alert(context.getString(R.string.input_habit_name)).show()
            return null
        }

        var newHabit = Habit(UUID.randomUUID().toString(),
                title,
                itemColor
        )
        dismiss()
        return newHabit
    }

    fun modifyHabit(): Habit? {
        var title = et_add_habit_title.text.toString()
        if (title == "") {
            // 제목을 안지었을 때
            context.alert(context.getString(R.string.input_habit_name)).show()
            return null
        }
        habit?.title = title
        habit?.color = itemColor
        dismiss()
        return habit
    }
}