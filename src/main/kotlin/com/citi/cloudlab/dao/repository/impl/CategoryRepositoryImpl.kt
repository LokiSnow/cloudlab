package com.citi.cloudlab.dao.repository.impl

import com.citi.cloudlab.dao.model.Category
import com.citi.cloudlab.dao.repository.CategoryRepository

/**
 *
 *@author Loki
 *@date 2023/5/11
 */
class CategoryRepositoryImpl : BaseRepositoryImpl<Category>(Category::class.java), CategoryRepository {
}