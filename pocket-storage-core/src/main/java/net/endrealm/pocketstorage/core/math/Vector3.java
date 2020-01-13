package net.endrealm.pocketstorage.core.math;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.function.Consumer;

@Data
@AllArgsConstructor
public class Vector3 implements Cloneable {
    private double x, y, z;

    @Override
    protected Vector3 clone() throws CloneNotSupportedException {
        return (Vector3) super.clone();
    }

    public void loop(Vector3 other, double interval, Consumer<Vector3> action) {
        for(double x = this.x; x <= other.x; x += interval) {
            for(double y = this.y; y <= other.y; y += interval) {
                for(double z = this.z; z <= other.z; z += interval) {
                    action.accept(new Vector3(x, y, z));
                }
            }
        }
    }

    public int getIntX() {
        return (int) x;
    }

    public int getIntY() {
        return (int) y;
    }

    public int getIntZ() {
        return (int) z;
    }

    public double getIterationSize(int interval) {
        return x/interval*y/interval*z/interval;
    }

    public int getIterationSizeInt(int interval) {
        return (int) Math.ceil(getIterationSize(interval));
    }
}
