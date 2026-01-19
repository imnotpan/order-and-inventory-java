package de.imnotpan.orderinventory.persistence.inventory;

import de.imnotpan.orderinventory.domain.inventory.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long>{
}
