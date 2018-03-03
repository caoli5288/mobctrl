package com.i5mc.mobctrl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.EntityType;

import java.util.Map;

@EqualsAndHashCode(of = "mobType")
@Data
public class MobCtrlInf {

    private EntityType mobType;
    private double maxHealth;
    private transient Map<?, ?> handle;
}
