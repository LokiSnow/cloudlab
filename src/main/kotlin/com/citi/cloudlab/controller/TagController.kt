package com.citi.cloudlab.controller

import com.citi.cloudlab.dao.model.Tag
import com.citi.cloudlab.response.BaseResponse
import com.citi.cloudlab.service.TagService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tags")
class TagController(
    val service: TagService
) {
    @PostMapping
    suspend fun save(@RequestBody tag: Tag): BaseResponse<Any> {
        return BaseResponse(service.save(tag))
    }

    @PutMapping("/{id}")
    suspend fun update(@PathVariable id: String, tag: Tag) {
        service.update(tag)
    }

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: String) {
        service.delete(id)
    }

    @GetMapping
    suspend fun findAll(@RequestParam(required = false) last: String?) : BaseResponse<Any> = BaseResponse(service.findAll(last))

    @GetMapping("/{id}/posts")
    suspend fun findPosts(@PathVariable id: String, @RequestParam(required = false)lastPostId: String?) : BaseResponse<Any> = BaseResponse(service.findPosts(id, lastPostId))

}