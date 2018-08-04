package com.riglerr.data.repositories;

import com.riglerr.domain.entities.Alert;
import com.riglerr.domain.interfaces.AlertRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryAlertRepository implements AlertRepository {

    private List<Alert> alertStore;

    public InMemoryAlertRepository(List<Alert> alertStore) {
        alertStore = new ArrayList<>();
    }

    @Override
    public void add(Alert newAlert) {
        alertStore.add(newAlert);
    }
}
