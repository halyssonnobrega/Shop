package com.demo.shop.business.service;

import com.demo.shop.business.entity.ItemEO;
import com.demo.shop.business.exception.custom.InvalidPriceException;
import com.demo.shop.business.exception.custom.NotFoundItemException;
import com.demo.shop.business.repository.ItemCustomRepository;
import com.demo.shop.business.repository.ItemRepository;
import com.demo.shop.config.jms.producers.ShopJmsProducer;
import com.demo.shop.resources.model.ItemRequest;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ShopService {

    private final ItemRepository itemRepository;

    private final ItemCustomRepository itemCustomRepository;

    private final ShopJmsProducer shopJmsProducer;

    @Autowired
    public ShopService(ItemRepository itemRepository, ItemCustomRepository itemCustomRepository, ShopJmsProducer shopJmsProducer) {
        this.itemRepository = itemRepository;
        this.itemCustomRepository = itemCustomRepository;
        this.shopJmsProducer = shopJmsProducer;
    }

    @Transactional
    public ItemEO insert(ItemRequest item) {
        if(item.getPrice() <= 0) {
            throw new InvalidPriceException();
        }

        ItemEO itemEO = new ItemEO();
        itemEO.setName(item.getName());
        itemEO.setPrice(item.getPrice());
        itemEO.setCreatedOnDate(new Date());

        return itemRepository.save(itemEO);
    }

    @Transactional
    public void insertBlock(List<ItemRequest> itens) {
        List<String> msg = new ArrayList<>();

        for (ItemRequest item : itens) {
            if(item.getPrice() <= 0) {
                throw new InvalidPriceException();
            } else {
                msg.add(new Gson().toJson(item));
            }
        }

        msg.stream().forEach(s -> shopJmsProducer.sendMessage(s));
    }

    @Transactional
    public void delete(Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
        } else {
            throw new NotFoundItemException();
        }
    }

    public ItemEO update(Long id, ItemRequest item){
        Optional<ItemEO> itemUpdate = itemRepository.findById(id);

        if (itemUpdate.isPresent()) {
            itemUpdate.get().setName(item.getName());
            itemUpdate.get().setPrice(item.getPrice());
            return itemRepository.save(itemUpdate.get());
        } else {
            throw new NotFoundItemException();
        }
    }

    public List<ItemEO> fetchAll(){
        return itemRepository.findAll();
    }

    public List<ItemEO> fetchByFilter(Long id, String name, Double price){
        List<ItemEO> itens = new ArrayList<>();

        itens.addAll(itemCustomRepository.findItensByFilters(id, name, price));

        if(!itens.isEmpty()){
            return itens;
        } else {
            throw new NotFoundItemException();
        }
    }
}
