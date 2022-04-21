package com.github.mkopylec.storage.utils.api.responses

class ContainerView {

    String identifier
    BigDecimal maximumWeightInKilograms
    int itemsQuantity
    BigDecimal itemsWeightInKilograms
    List<ItemView> items

    static class ItemView {

        UUID identifier
        String name
        BigDecimal weightInKilograms
    }
}
