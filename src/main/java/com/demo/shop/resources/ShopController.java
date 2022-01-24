package com.demo.shop.resources;

import com.demo.shop.business.service.ShopService;
import com.demo.shop.resources.model.ItemRequest;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.HttpURLConnection;
import java.util.List;

@Validated
@RestController
@RequestMapping("/shop")
@Api(tags = {"Shop API"})
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@PreAuthorize("isAuthenticated()")
public class ShopController {

    private final ShopService shopService;

    @Autowired
    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/createItem")
    @ApiOperation(value = "Create item")
    @ApiResponses({
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "OK"),
            @ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "INTERNAL ERROR")})
    public ResponseEntity<Object> createItem(@Valid @RequestBody
                                                 @ApiParam(name = "item", value = "Item to add")
                                                     ItemRequest item) {
        return ResponseEntity.ok(shopService.insert(item));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/createItens")
    @ApiOperation(value = "Create itens")
    @ApiResponses({
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "OK"),
            @ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "INTERNAL ERROR")})
    public ResponseEntity<Object> createItens(@Valid @RequestBody
                                                  @ApiParam(name = "itens", value = "Itens to add", required = true)
                                                      List<ItemRequest> itens) {
        shopService.insertBlock(itens);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/deleteItem/{id}")
    @ApiOperation(value = "Delete item by id")
    @ApiResponses({
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "OK"),
            @ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "NOT FOUND")})
    public ResponseEntity<Object> deleteItem(@PathVariable
                                                 @ApiParam(name = "id", value = "Item id", example = "1", required = true)
                                                 @NotNull Long id) {
        shopService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/updateItem/{id}")
    @ApiOperation(value = "Update item")
    @ApiResponses({
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "OK"),
            @ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "NOT FOUND")})
    public ResponseEntity<Object> updateItem(@PathVariable
                                                @ApiParam(name = "id", value = "Item id", example = "1", required = true)
                                                @NotNull Long id,
                                             @RequestBody ItemRequest item) {
        return ResponseEntity.ok(shopService.update(id, item));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/getItemByFilter")
    @ApiResponses({
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "OK"),
            @ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "NOT FOUND")})
    public ResponseEntity<Object> getItemByFilter(@RequestParam(required = false)
                                                      @ApiParam(name = "id", value = "Item id")
                                                              Long id,
                                                  @RequestParam(required = false)
                                                    @ApiParam(name = "name", value = "Item name")
                                                          String name,
                                                  @RequestParam(required = false)
                                                      @ApiParam(name = "price", value = "Max price")
                                                              Double price) {
        return ResponseEntity.ok(shopService.fetchByFilter(id, name, price));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/getItens")
    @ApiResponses({
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "OK")})
    public ResponseEntity<Object> getItens() {
        return ResponseEntity.ok(shopService.fetchAll());
    }
}
