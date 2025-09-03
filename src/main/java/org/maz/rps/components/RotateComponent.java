package org.maz.rps.components;

import com.almasb.fxgl.entity.component.Component;

public class RotateComponent extends Component {
    private static final double speed = 0.3;

    @Override
    public void onUpdate(double tpf) {
        entity.rotateBy(speed);
    }
}
