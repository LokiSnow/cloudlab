package com.citi.cloudlab.controller

import com.citi.cloudlab.dao.model.Category
import com.citi.cloudlab.response.BaseResponse
import com.citi.cloudlab.service.CategoryService
import org.springframework.web.bind.annotation.*
@RestController
@RequestMapping("/categories")
class CategoryController(
    val service: CategoryService
) {
    @PostMapping
    suspend fun save(@RequestBody category: Category): BaseResponse<Any> {
        return BaseResponse(service.save(category))
    }

    @PutMapping("/{id}")
    suspend fun update(@PathVariable id: String, category: Category) {
        service.update(category)
    }

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: String) {
        service.delete(id)
    }

    @GetMapping
    suspend fun findAll(@RequestParam(required = false) last: String?) : BaseResponse<Any> = BaseResponse(service.findAll(last))

}