package com.demo.shop.resources;

import com.demo.shop.business.entity.ItemEO;
import com.demo.shop.business.exception.custom.InvalidPriceException;
import com.demo.shop.business.exception.custom.NotFoundItemException;
import com.demo.shop.business.repository.ItemRepository;
import com.demo.shop.business.service.ShopService;
import com.demo.shop.resources.model.ItemRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ShopControllerTests {

	@InjectMocks
	private ShopService shopService;

	@Mock
	private ItemRepository itemRepository;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testInsertItemWithInvalidPrice() {
		ItemRequest item = new ItemRequest();
		item.setName("Item Test");
		item.setPrice(0.0);

		try{
			shopService.insert(item);
		} catch(InvalidPriceException e){
			Assert.assertTrue(true);
		} catch(Exception e){
			Assert.fail("wrong exception thrown");
		}
	}

	@Test
	void testInsertItemWithValidPrice() {
		ItemRequest item = new ItemRequest();
		item.setName("Item Test");
		item.setPrice(1.0);

		ItemEO itemEO_create = new ItemEO();
		itemEO_create.setId(new Long(1));
		itemEO_create.setName("Item Test");
		itemEO_create.setPrice(1.0);
		itemEO_create.setCreatedOnDate(new Date());
		itemEO_create.setVersion(0);

		Mockito.when(itemRepository.save(Mockito.any(ItemEO.class))).thenReturn(itemEO_create);
		ItemEO newItem = shopService.insert(item);

		Assert.assertEquals(newItem.getName(), item.getName());
		Assert.assertEquals(new Double(newItem.getPrice()), item.getPrice());
	}

	@Test
	void testUpdateItemWithValidId() {
		ItemRequest item = new ItemRequest();
		item.setName("Item Test Update");
		item.setPrice(1.0);

		ItemEO itemEO_update = new ItemEO();
		itemEO_update.setId(new Long(1));
		itemEO_update.setName("Item Test Update");
		itemEO_update.setPrice(1.0);
		itemEO_update.setCreatedOnDate(new Date());
		itemEO_update.setVersion(0);

		Mockito.when(itemRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(itemEO_update));
		Mockito.when(itemRepository.save(Mockito.any(ItemEO.class))).thenReturn(itemEO_update);
		ItemEO itemUpdate = shopService.update(new Long(1), item);

		Assert.assertEquals(itemUpdate.getName(), item.getName());
	}

	@Test
	void testUpdateItemWithInvalidId() {
		try{
			Mockito.when(itemRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
			shopService.update(new Long(2), Mockito.any(ItemRequest.class));
		} catch(NotFoundItemException e){
			Assert.assertTrue(true);
		} catch(Exception e){
			Assert.fail("wrong exception thrown");
		}
	}

	@Test
	void testFetchAllItens() {
		List<ItemEO> itemList = getItemList();
		Mockito.when(itemRepository.findAll()).thenReturn(itemList);
		List<ItemEO> list = shopService.fetchAll();

		Assert.assertEquals(itemList.get(0), list.get(0));
		Assert.assertNotEquals(itemList.get(0), list.get(1));
		Mockito.verify(itemRepository).findAll();
	}

	public List<ItemEO> getItemList() {
		List itens = new ArrayList();

		ItemEO itemEO_1 = new ItemEO();
		itemEO_1.setId(new Long(1));
		itemEO_1.setName("Item Test 1");
		itemEO_1.setPrice(1.0);
		itemEO_1.setCreatedOnDate(new Date());
		itemEO_1.setVersion(0);

		itens.add(itemEO_1);

		ItemEO itemEO_2 = new ItemEO();
		itemEO_2.setId(new Long(3));
		itemEO_2.setName("Item Test 2");
		itemEO_2.setPrice(2.0);
		itemEO_2.setCreatedOnDate(new Date());
		itemEO_2.setVersion(0);

		itens.add(itemEO_2);

		return itens;
	}
}
