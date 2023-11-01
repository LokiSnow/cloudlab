package com.citi.cloudlab.service.impl

import com.citi.cloudlab.dao.model.Category
import com.citi.cloudlab.dao.repository.CategoryRepository
import com.citi.cloudlab.service.CategoryService
import org.springframework.stereotype.Component

@Component
class CategoryServiceImpl (
    override val repository: CategoryRepository
): BaseServiceImpl<Category>(repository), CategoryService {
}