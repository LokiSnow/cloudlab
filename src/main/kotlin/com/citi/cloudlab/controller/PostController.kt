package com.citi.cloudlab.controller

import com.citi.cloudlab.response.BaseResponse
import com.citi.cloudlab.service.PostService
import com.nebula.dao.model.Post
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostController(
    val postService: PostService
) {
    val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping
    suspend fun save(@RequestBody post: Post): BaseResponse<Any> {
        return BaseResponse(postService.save(post))
    }

    @PutMapping("/{id}")
    suspend fun update(@PathVariable id: String, post: Post) {
        postService.update(post)
    }

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: String) {
        postService.delete(id)
    }

    @GetMapping
    suspend fun findAll(@RequestParam(required = false) last: String?) : BaseResponse<Any> = BaseResponse(postService.findAll(last))

    @GetMapping("/{id}")
    suspend fun findById(@PathVariable id: String) : BaseResponse<Any> = BaseResponse(postService.findById(id))

    @GetMapping("/categories/{categoryCode}")
    suspend fun findByCategory(@PathVariable categoryCode: String, @RequestParam(required = false) last: String?) : BaseResponse<Any> = BaseResponse(postService.findByCategory(categoryCode, last))

}