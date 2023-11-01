package com.citi.cloudlab.controller

import com.citi.cloudlab.dao.model.Comment
import com.citi.cloudlab.response.BaseResponse
import com.citi.cloudlab.service.CommentService
import org.springframework.web.bind.annotation.*
@RestController
@RequestMapping("/comments")
class CommentController(
    val service: CommentService
) {
    @PostMapping
    suspend fun save(@RequestBody comment: Comment): BaseResponse<Any> {
        return BaseResponse(service.save(comment))
    }

    @PutMapping("/{id}")
    suspend fun update(@PathVariable id: String, comment: Comment) {
        service.update(comment)
    }

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: String) {
        service.delete(id)
    }

    @GetMapping("/{postId}")
    suspend fun findByPostId(@PathVariable postId: String, @RequestParam(required = false) last: String?) : BaseResponse<Any> = BaseResponse(service.findByPostId(postId, last))

}