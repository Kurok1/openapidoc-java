/*
 * Copyright [2024] [Kurok1]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.kurok1.processing.impl.spring;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
interface SpringAnnotations {
    
    String ANNOTATION_REST_CONTROLLER = "org.springframework.web.bind.annotation.RestController";
    String ANNOTATION_CONTROLLER = "org.springframework.stereotype.Controller";
    
    String ANNOTATION_REQUEST_MAPPINGS = "org.springframework.web.bind.annotation.RequestMapping";
    String ANNOTATION_GET_MAPPINGS = "org.springframework.web.bind.annotation.GetMapping";
    String ANNOTATION_POST_MAPPINGS = "org.springframework.web.bind.annotation.PostMapping";
    String ANNOTATION_PUT_MAPPINGS = "org.springframework.web.bind.annotation.PutMapping";
    String ANNOTATION_DELETE_MAPPINGS = "org.springframework.web.bind.annotation.DeleteMapping";
    
    String ANNOTATION_REQUEST_BODY = "org.springframework.web.bind.annotation.RequestBody";
    String ANNOTATION_RESPONSE_BODY = "org.springframework.web.bind.annotation.ResponseBody";
    String ANNOTATION_REQUEST_PARAM = "org.springframework.web.bind.annotation.RequestParam";
    String ANNOTATION_PATH_VARIABLE = "org.springframework.web.bind.annotation.PathVariable";
    String ANNOTATION_REQUEST_HEADER = "org.springframework.web.bind.annotation.RequestHeader";
    
    
}
