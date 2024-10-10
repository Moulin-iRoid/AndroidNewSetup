package com.example.androidnewsetup.data.bean.pagingcommondata

import com.google.gson.annotations.SerializedName

data class Links(
    @SerializedName("first") val first: String,
    @SerializedName("last") val last: String,
    @SerializedName("next") val next: String,
    @SerializedName("prev") val prev: String
)

data class Meta(
    @SerializedName("current_page") val currentPage: Int = 0,
    @SerializedName("from") val from: Int = 0,
    @SerializedName("last_page") val lastPage: Int = 0,
    @SerializedName("links") val links: List<Link> = listOf(),
    @SerializedName("path") val path: String = "",
    @SerializedName("per_page") val perPage: String = "",
    @SerializedName("to") val to: Int = 0,
    @SerializedName("total") val total: Int = 0
)

data class Link(
  @SerializedName("active") val active: Boolean,
  @SerializedName("label") val label: String,
  @SerializedName("url") val url: String
)