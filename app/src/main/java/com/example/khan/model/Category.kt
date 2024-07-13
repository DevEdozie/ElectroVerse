package com.example.khan.model

data class Category(
    val category_type: String,
    val date_created: String,
    val description: String,
    val id: String,
    val is_deleted: Boolean,
    val last_updated: String,
    val name: String,
    val organization_id: String,
    val parent_id: Any,
    val parents: List<Any>,
    val position: Any,
    val subcategories: List<Any>,
    val url_slug: Any
)