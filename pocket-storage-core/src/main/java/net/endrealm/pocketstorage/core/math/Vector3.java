package net.endrealm.pocketstorage.core.math;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vector3 implements Cloneable {
    private double x, y, z;

    @Override
    protected Vector3 clone() throws CloneNotSupportedException {
        return (Vector3) super.clone();
    }
}
