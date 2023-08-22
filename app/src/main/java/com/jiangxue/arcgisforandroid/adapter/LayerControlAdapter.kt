package com.jiangxue.arcgisforandr

import android.os.Parcel
import android.os.Parcelable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.CompoundButton
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.TextView
import com.jiangxue.arcgisforandroid.R
import com.jiangxue.arcgisforandroid.data.LayerCategoryBean
import com.jiangxue.arcgisforandroid.data.xml.shpproperties.Shp
import com.jiangxue.arcgisforandroid.mapview.MapViewHelper
import com.jiangxue.arcgisforandroid.widge.MapViewObserver
import com.rey.material.widget.CheckBox
import com.rey.material.widget.Slider
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe

/**
 * 图层控制适配器
 */
class LayerControlAdapter(mapViewObserver: MapViewHelper?) : BaseExpandableListAdapter() {
    private var layerCategoryBeanList //所有图层数据
            : List<LayerCategoryBean>? = null
    private var groupViewHolder: GroupViewHolder? = null
    private var childViewHolder: ChildViewHolder? = null
    private val mapViewObserver: MapViewObserver

    init {
        this.mapViewObserver = mapViewObserver!!
    }

    fun setData(layerCategoryBeanList: List<LayerCategoryBean>) {
        this.layerCategoryBeanList = layerCategoryBeanList
        notifyDataSetChanged()
    }


    override fun getGroupCount(): Int {
        return if (layerCategoryBeanList == null) 0 else layerCategoryBeanList!!.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return if (layerCategoryBeanList == null) 0 else if (layerCategoryBeanList!![groupPosition] == null) 0 else layerCategoryBeanList!![groupPosition].getLayers()!!.size
    }

    override fun getGroup(groupPosition: Int): LayerCategoryBean {
        return layerCategoryBeanList!![groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Shp {
        return layerCategoryBeanList!![groupPosition].getLayers()!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return 0
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return 0
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView: View? = convertView
        groupViewHolder = null
        if (convertView == null) {
            groupViewHolder = GroupViewHolder()
            convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layer_control_group, null)
            groupViewHolder!!.iv_status =
                convertView.findViewById<View>(R.id.iv_status) as ImageView
            groupViewHolder!!.tv_name = convertView.findViewById<View>(R.id.tv_name) as TextView
            groupViewHolder!!.cb_isChecked =
                convertView.findViewById<View>(R.id.cb_isChecked) as CheckBox
            convertView.tag = groupViewHolder
        } else {
            groupViewHolder = convertView.tag as GroupViewHolder
        }
        val group = getGroup(groupPosition)
        if (isExpanded) {
            groupViewHolder!!.iv_status!!.setImageResource(R.mipmap.icon_layer_list_selected)
        } else {
            groupViewHolder!!.iv_status!!.setImageResource(R.mipmap.icon_layer_list_normal)
        }
        groupViewHolder!!.tv_name!!.text = group.getName()
        groupViewHolder!!.cb_isChecked!!.setOnCheckedChangeListener(null)
        groupViewHolder!!.cb_isChecked!!.setCheckedImmediately(group.isChecked())
        GroupChangeCheckStatus(
            group,
            groupPosition
        ).changeCheckStatus(groupViewHolder!!.cb_isChecked)
        ExpandGroup(groupPosition, isExpanded, convertView!!, parent, groupViewHolder!!.iv_status)
        return convertView
    }

    private inner class ExpandGroup(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View,
        parent: ViewGroup,
        iv_status: ImageView?
    ) : View.OnClickListener {
        private val groupPosition: Int
        private val parent: ViewGroup
        private val isExpanded: Boolean
        private val iv_status: ImageView?

        init {
            convertView.setOnClickListener(this)
            this.groupPosition = groupPosition
            this.parent = parent
            this.isExpanded = isExpanded
            this.iv_status = iv_status
        }

        override fun onClick(v: View) {
            if (isExpanded) {
                (parent as ExpandableListView).collapseGroup(groupPosition)
                iv_status!!.setImageResource(R.mipmap.icon_layer_list_selected)
            } else {
                (parent as ExpandableListView).expandGroup(groupPosition, true)
                iv_status!!.setImageResource(R.mipmap.icon_layer_list_normal)
            }
        }
    }

  /*  override fun getChildView(p0: Int, p1: Int, p2: Boolean, p3: View?, p4: ViewGroup?): View {
        TODO("Not yet implemented")
    }*/
   override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var convertView: View? = convertView
        childViewHolder = null
        if (convertView == null) {
            childViewHolder = ChildViewHolder()
            convertView = LayoutInflater.from(parent?.getContext() )
                .inflate(R.layout.item_layer_control_child, null)
            childViewHolder!!.tv_name = convertView.findViewById<View>(R.id.tv_name) as TextView
            childViewHolder!!.iv_full = convertView.findViewById<View>(R.id.iv_full) as ImageView
            childViewHolder!!.cb_isChecked =
                convertView.findViewById<View>(R.id.cb_isChecked) as CheckBox
            childViewHolder!!.layer_icon =
                convertView.findViewById<View>(R.id.layer_icon) as ImageView
            childViewHolder!!.iv_color_setting =
                convertView.findViewById<View>(R.id.iv_color_setting) as ImageView
            childViewHolder!!.slider_alpha =
                convertView.findViewById<View>(R.id.slider_alpha) as Slider
            convertView.tag = childViewHolder
        } else {
            childViewHolder = convertView.tag as ChildViewHolder
        }
        val child: Shp = getChild(groupPosition, childPosition)
        childViewHolder!!.tv_name?.text=child.name
        childViewHolder!!.cb_isChecked!!.setOnCheckedChangeListener(null)
        childViewHolder!!.cb_isChecked!!.setCheckedImmediately(child.isChecked)
        //Glide.with(parent.getContext()).load(child.getIconPath()).placeholder(R.mipmap.icon_setting_info).into(childViewHolder.layer_icon);

        return convertView!!
    }

    private inner class GroupChangeCheckStatus(
        private val group: LayerCategoryBean,
        private val groupPosition: Int
    ) : CompoundButton.OnCheckedChangeListener {
        fun changeCheckStatus(checkBox: CheckBox?) {
            checkBox!!.setOnCheckedChangeListener(this)
        }

        override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {

            //点击选中显示所有的shp格式
            group.setChecked(isChecked)
            for (shp in group.getLayers()!!) {
                shp.isChecked=isChecked
            }
            val layers: List<Shp>? = layerCategoryBeanList!![groupPosition].getLayers()
            for (shp in layers!!) {

                Observable.create(object : ObservableOnSubscribe<ActionGuide> {
                    override fun subscribe(e: ObservableEmitter<ActionGuide>) {
                        e.onNext(ActionGuide(shp, if (isChecked) 1 else 2))
                    }

                }).subscribe(mapViewObserver)
            }
            this@LayerControlAdapter.notifyDataSetChanged()
        }
    }


    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    private class GroupViewHolder {
        var iv_status: ImageView? = null
        var tv_name: TextView? = null
        var cb_isChecked: CheckBox? = null
    }

    private class ChildViewHolder {
        var tv_name: TextView? = null
        var cb_isChecked: CheckBox? = null
        var iv_full: ImageView? = null
        var layer_icon: ImageView? = null
        var iv_color_setting: ImageView? = null
        var slider_alpha: Slider? = null
    }

    class ActionGuide : Parcelable {
        private var shp: Shp

        /**
         * 0 不变    1 显示   2 隐藏
         *
         * @return
         */
        var guide: Int
            private set

        constructor(shp: Shp, guide: Int) {
            this.shp = shp
            this.guide = guide
        }

        val layerId: Long
            get() = shp.id
        val color: Int
            get() = shp.getLayerColor()
        val isContainTpk: Boolean
            get() = shp.isContainTpk
        val tpkId: Long
            get() = shp.tpkId
        val tpkName: String
            get() = shp.tpkName.toString()
        val layerName: String
            get() = shp.name.toString()

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeParcelable(shp, flags)
            dest.writeInt(guide)
        }

        protected constructor(`in`: Parcel) {
            shp = `in`.readParcelable(Shp::class.java.getClassLoader())!!
            guide = `in`.readInt()
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<ActionGuide?> =
                object : Parcelable.Creator<ActionGuide?> {
                    override fun createFromParcel(source: Parcel): ActionGuide {
                        return ActionGuide(source)
                    }

                    override fun newArray(size: Int): Array<ActionGuide?> {
                        return arrayOfNulls(size)
                    }
                }
        }
    }
}