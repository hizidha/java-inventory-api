package item.service;

import exception.ItemAlreadyExistsException;
import exception.ItemNotFoundException;
import exception.FailedToUpdateItemDataException;

import exception.NoDataHasChangedException;
import item.model.Item;
import item.repository.ItemRepository;

import itemhistory.model.ItemHistory;
import itemhistory.model.Type;
import itemhistory.service.ItemHistoryService;

import java.util.List;

public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemHistoryService itemHistoryService;

    public ItemService(ItemRepository itemRepository, ItemHistoryService itemHistoryService) {
        this.itemRepository = itemRepository;
        this.itemHistoryService = itemHistoryService;
    }

    public List<Item> getAll() {
        return this.itemRepository.getAll();
    }

    public Item getBy(int id) {
        return this.itemRepository.findItemby(id)
                .orElseThrow(ItemNotFoundException::new);
    }

    public int deleteBy(int id) {
        return this.itemRepository.delete(id);
    }

    public int add(Item item) {
        int existingItem = this.itemRepository.checkItem(item.getName());
        if (existingItem > 0) {
            throw new ItemAlreadyExistsException();
        }

        int result = this.itemRepository.add(item);
        if (result <= 0) {
            throw new FailedToUpdateItemDataException();
        }

        ItemHistory newItemHistory = ItemHistory.createInbound(item, item.getQuantity());
        this.itemHistoryService.create(newItemHistory);

        return result;
    }

    public int update(Item item, int quantity) {
        Item existingItem = this.getBy(item.getId());

        boolean isNameDifferent = !existingItem.getName().equals(item.getName());
        boolean isQuantityDifferent = existingItem.getQuantity() != quantity;
        if (!isNameDifferent && !isQuantityDifferent) {
            throw new NoDataHasChangedException();
        }

        existingItem = isQuantityDifferent ? existingItem.setQuantity(quantity) : existingItem;

        int result = itemRepository.update(existingItem);
        if (result <= 0) {
            throw new FailedToUpdateItemDataException();
        }

        ItemHistory newItemHistory = ItemHistory.createInbound(existingItem, existingItem.getQuantity());
        this.itemHistoryService.create(newItemHistory);

        return result;
    }

    public int buy(Item item, int quantity) {
        Item existingItem = this.getBy(item.getId()).increaseQuantity(quantity);
        int result = this.itemRepository.update(existingItem);
        if (result <= 0) {
            throw new FailedToUpdateItemDataException();
        }

        ItemHistory newItemHistory = ItemHistory.createInbound(existingItem, existingItem.getQuantity());
        this.itemHistoryService.create(newItemHistory);

        return result;
    }

    public int sell(Item item, int quantity) {
        Item existingItem = this.getBy(item.getId()).decreaseQuantity(quantity);
        int result = this.itemRepository.update(existingItem);
        if (result <= 0) {
            throw new FailedToUpdateItemDataException();
        }

        ItemHistory newItemHistory = ItemHistory.createOutbound(existingItem, existingItem.getQuantity());
        this.itemHistoryService.create(newItemHistory);

        return result;
    }
}