package com.example.floClone.core.ui.vp

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.floClone.feature.main.home.panel.PanelFragment

class PanelVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return PanelFragment()
            1 -> return PanelFragment()
            2 -> return PanelFragment()
            3 -> return PanelFragment()
            else -> return PanelFragment()
        }
    }

}