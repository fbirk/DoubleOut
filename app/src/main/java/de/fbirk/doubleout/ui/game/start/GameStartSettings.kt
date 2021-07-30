package de.fbirk.doubleout.ui.game.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.activityViewModels
import de.fbirk.doubleout.R
import de.fbirk.doubleout.model.FinishMode


class GameStartSettings : Fragment() {

    // private val viewModel: GameStartViewModel by activityViewModels()
    var mStartingValue = 301
    var mFinishMode: FinishMode = FinishMode.STRAIGHT_OUT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game_start_settings, container, false)

        val radioStartingValue = view.findViewById<RadioGroup>(R.id.radio_startingValue)
        val radioBtn301 = view.findViewById<RadioButton>(R.id.radioBtn_301)
        val radioBtn501 = view.findViewById<RadioButton>(R.id.radionBtn_501)
        radioStartingValue.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            if (radioBtn301.isChecked) {
                mStartingValue = 301
            } else if (radioBtn501.isChecked) {
                mStartingValue = 501
            }
        }

        val radioFinishMode = view.findViewById<RadioGroup>(R.id.radio_finishMode)
        val radioBtnStraightOut = view.findViewById<RadioButton>(R.id.radioBtn_StraightOut)
        val radioBtnDoubleOut = view.findViewById<RadioButton>(R.id.radioBtn_DoubleOut)
        val radioBtnMasterOut = view.findViewById<RadioButton>(R.id.radioBtn_MasterOut)
        val radioBtnDoubleIn = view.findViewById<RadioButton>(R.id.radioBtn_DoubleIn)
        radioFinishMode.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            if (radioBtnStraightOut.isChecked) {
                mFinishMode = FinishMode.STRAIGHT_OUT
            } else if (radioBtnDoubleOut.isChecked) {
                mFinishMode = FinishMode.DOUBLE_OUT
            } else if (radioBtnMasterOut.isChecked) {
                mFinishMode = FinishMode.MASTER_OUT
            } else if (radioBtnDoubleIn.isChecked) {
                mFinishMode = FinishMode.DOUBLE_IN
            }
        }

        return view
    }
}