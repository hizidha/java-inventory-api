package itemhistory.service;

import exception.FailedToCreateItemHistoryException;
import exception.ItemHistoryNotFoundException;

import item.model.Item;

import itemhistory.model.Type;
import itemhistory.model.ItemHistory;
import itemhistory.repository.ItemHistoryRepository;

import java.util.List;

public class ItemHistoryService {
    private final ItemHistoryRepository itemHistoryRepository;

    public ItemHistoryService(ItemHistoryRepository itemHistoryRepository) {
        this.itemHistoryRepository = itemHistoryRepository;
    }

    public List<ItemHistory> getAll() {
        return this.itemHistoryRepository.getAll();
    }

    public ItemHistory getBy(int id) {
        return this.itemHistoryRepository.findItemby(id)
                .orElseThrow(ItemHistoryNotFoundException::new);
    }

    public int deleteBy(int id) {
        return this.itemHistoryRepository.delete(id);
    }

    public int update(ItemHistory itemHistory, Item item, Type type, int quantity) {
        ItemHistory existingItemHistory = this.getBy(itemHistory.getId());

        if (existingItemHistory == null) {
            throw new ItemHistoryNotFoundException();
        }

        ItemHistory updatedItemHistory = new ItemHistory(
                existingItemHistory.getId(),
                item.getId(),
                type,
                quantity
        );
        int result = this.itemHistoryRepository.update(updatedItemHistory);
        if (result <= 0) {
            throw new FailedToCreateItemHistoryException();
        }
        return 1;
    }

    public void create(ItemHistory newItemHistory) {
        int result = itemHistoryRepository.add(newItemHistory);

        if (result <= 0) {
            throw new FailedToCreateItemHistoryException();
        }
    }
}