package com.jiangxue.arcgisforandroid.adapter

import android.graphics.Color
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import com.jiangxue.arcgisforandroid.data.LayerCategoryBean
import com.rey.material.widget.CheckBox
import com.rey.material.widget.Slider
import io.reactivex.Observable

/**
 * Created by Jinyu Zhang on 2017/5/5.
 * 图层控制适配器
 */
class LayerControlAdapter(mapViewObserver: MapViewObserver) : BaseExpandableListAdapter() {
    private var layerCategoryBeanList //所有图层数据
            : List<LayerCategoryBean>? = null
    private var groupViewHolder: GroupViewHolder? = null
    private var childViewHolder: ChildViewHolder? = null
    private val mapViewObserver: MapViewObserver

    init {
        this.mapViewObserver = mapViewObserver
    }

    fun setData(layerCategoryBeanList: List<LayerCategoryBean>?) {
        this.layerCategoryBeanList = layerCategoryBeanList
        notifyDataSetChanged()
    }

    val groupCount: Int
        get() = if (layerCategoryBeanList == null) 0 else layerCategoryBeanList!!.size

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
        convertView: View,
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
        ExpandGroup(groupPosition, isExpanded, convertView, parent, groupViewHolder!!.iv_status)
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

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View,
        parent: ViewGroup
    ): View {
        var convertView: View? = convertView
        childViewHolder = null
        if (convertView == null) {
            childViewHolder = ChildViewHolder()
            convertView = LayoutInflater.from(parent.getContext())
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
        childViewHolder!!.tv_name.setText(child.getName())
        childViewHolder!!.cb_isChecked!!.setOnCheckedChangeListener(null)
        childViewHolder!!.cb_isChecked!!.setCheckedImmediately(child.isChecked())
        //Glide.with(parent.getContext()).load(child.getIconPath()).placeholder(R.mipmap.icon_setting_info).into(childViewHolder.layer_icon);
        val childStatusManage =
            ChildStatusManage(groupPosition, childPosition, layerCategoryBeanList)
        childStatusManage.changeCheckStatus(
            childViewHolder!!.cb_isChecked,
            childViewHolder!!.iv_full,
            childViewHolder!!.iv_color_setting,
            childViewHolder!!.slider_alpha
        )
        childStatusManage.full(childViewHolder!!.iv_full)
        if (childViewHolder!!.cb_isChecked!!.isChecked) {
            //childViewHolder.iv_full.setVisibility(View.VISIBLE);
            childViewHolder!!.slider_alpha!!.visibility = View.VISIBLE
            if (child.isContainTpk()) {
                childViewHolder!!.iv_color_setting!!.visibility = View.GONE
            } else {
                childViewHolder!!.iv_color_setting!!.visibility = View.GONE
            }
        } else {
            //childViewHolder.slider_alpha.setVisibility(View.GONE);
            //childViewHolder.iv_full.setVisibility(View.GONE);
            childViewHolder!!.iv_color_setting!!.visibility = View.GONE
        }
        childStatusManage.onColorSetting(childViewHolder!!.iv_color_setting)
        return convertView
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
                shp.setChecked(isChecked)
            }
            val layers: List<Shp>? = layerCategoryBeanList!![groupPosition].getLayers()
            for (shp in layers) {
                Observable.create<ActionGuide>(object : ObservableOnSubscribe<ActionGuide?> {
                    @Throws(Exception::class)
                    override fun subscribe(e: ObservableEmitter<ActionGuide>) {
                        e.onNext(ActionGuide(shp, if (isChecked) 1 else 2))
                    }
                }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.newThread())
                    .subscribe(mapViewObserver)
            }
            this@LayerControlAdapter.notifyDataSetChanged()
        }
    }

    private inner class ChildStatusManage(
        groupPosition: Int?,
        childPosition: Int?,
        layerCategoryBeanList: List<LayerCategoryBean>?
    ) : CompoundButton.OnCheckedChangeListener, View.OnClickListener, OnPositionChangeListener {
        private val shp: Shp
        private var fullView: View? = null
        private var colorSetView: View? = null
        private var slider: Slider? = null

        init {
            shp = layerCategoryBeanList!![groupPosition!!].getLayers()!![childPosition!!]
        }

        fun changeCheckStatus(
            radioButton: CheckBox?,
            fullView: View?,
            colorSetView: View?,
            slider: Slider?
        ) {
            this.fullView = fullView
            this.colorSetView = colorSetView
            this.slider = slider
            radioButton!!.setOnCheckedChangeListener(this)
            slider!!.setOnPositionChangeListener(this)
        }

        fun full(view: View?) {
            view!!.setOnClickListener(this)
        }

        override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
            Observable.create<ActionGuide>(object : ObservableOnSubscribe<ActionGuide?> {
                @Throws(Exception::class)
                override fun subscribe(e: ObservableEmitter<ActionGuide>) {
                    shp.setChecked(isChecked)
                    e.onNext(ActionGuide(shp, if (isChecked) 1 else 2))
                }
            }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.newThread())
                .subscribe(mapViewObserver)
        }

        override fun onClick(v: View) {
            if (layerControlListener == null) {
                return
            }
            when (v.id) {
                R.id.iv_full -> layerControlListener.wholePicture(shp)
                R.id.iv_color_setting -> layerControlListener.colorSet(shp)
            }
        }

        fun onColorSetting(view: View?) {
            view!!.setOnClickListener(this)
        }

        private fun updateColorWithAlpha(color: Int, alpha: Int): Int {
            val red = color and 0xff0000 shr 16
            val green = color and 0x00ff00 shr 8
            val blue = color and 0x0000ff
            return Color.argb(alpha, red, green, blue)
        }

        override fun onPositionChanged(
            view: Slider,
            fromUser: Boolean,
            oldPos: Float,
            newPos: Float,
            oldValue: Int,
            newValue: Int
        ) {
            if (shp.isContainTpk()) {
                if ((mapViewObserver as MapViewHelper).getTpkLayer(shp) != null) {
                    (mapViewObserver as MapViewHelper).getTpkLayer(shp)
                        .setOpacity(newValue * 1f / 255)
                }
            }
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
            get() = shp.getId()
        val color: Int
            get() = shp.getLayerColor()
        val isContainTpk: Boolean
            get() = shp.isContainTpk()
        val tpkId: Long
            get() = shp.getTpkId()
        val tpkName: String
            get() = shp.getTpkName()
        val layerName: String
            get() = shp.getName()

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeParcelable(shp, flags)
            dest.writeInt(guide)
        }

        protected constructor(`in`: Parcel) {
            shp = `in`.readParcelable(Shp::class.java.getClassLoader())
            guide = `in`.readInt()
        }

        companion object {
            val CREATOR: Parcelable.Creator<ActionGuide> =
                object : Parcelable.Creator<ActionGuide?> {
                    override fun createFromParcel(source: Parcel): ActionGuide {
                        return ActionGuide(source)
                    }

                    override fun newArray(size: Int): Array<ActionGuide> {
                        return arrayOfNulls(size)
                    }
                }
        }
    }
}