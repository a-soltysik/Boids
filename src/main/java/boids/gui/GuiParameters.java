package boids.gui;

public class GuiParameters {
    public static volatile String fileName = "plik.csv";
    /*public static volatile int preyNumber = 200;
    public static volatile int predatorNumber = 2;
    public static volatile int obstacleNumber = 0;
    public static volatile float predatorMaxSpeed = 30f;
    public static volatile float predatorMaxAcceleration = 8f;
    public static volatile float preyMaxSpeed = 50f;
    public static volatile float preyMaxAcceleration = 10f;
    public static volatile float preyAlignmentWeight = 1.5f;
    public static volatile float preySeparationWeight = 2f;
    public static volatile float preyCohesionWeight = 1.5f;
    public static volatile float backGroundSpeed = 0.1f;
    public static volatile boolean writeToFile = false;*/

    public static final IntegerParameter preyNumber = new IntegerParameter("Liczba ptaków", 200, 0, 1000, 10);
    public static final IntegerParameter predatorNumber = new IntegerParameter("Liczba drapieżników", 0, 0, 10, 1);
    public static final IntegerParameter obstacleNumber = new IntegerParameter("Liczba przeszkód", 0, 0, 10, 1);
    public static final FloatParameter predatorMaxSpeed = new FloatParameter(50f);
    public static final FloatParameter predatorMaxAcceleration = new FloatParameter(8f);
    public static final FloatParameter preyMaxSpeed = new FloatParameter("Maksymalna szybkość ptaków", 50f, 20f, 120f, 1f);
    public static final FloatParameter preyMaxAcceleration = new FloatParameter(10f);
    public static final FloatParameter preyAlignmentWeight = new FloatParameter("Waga wyrównania", 1.5f, 0f, 10f, 0.1f);
    public static final FloatParameter preySeparationWeight = new FloatParameter("Waga separacji", 2f, 0f, 10f, 0.1f);
    public static final FloatParameter preyCohesionWeight = new FloatParameter("Waga spójności", 1.5f, 0f, 10f, 0.1f);
    public static final FloatParameter backGroundSpeed = new FloatParameter("Szybkość tła", 0.05f, 0f, 1f, 0.01f);
    public static final FloatParameter preyFovRadius = new FloatParameter("Promień pola widzenia ptaków", 60f, 10f, 150f, 1f);
    public static final FloatParameter preyFovAngleDeg = new FloatParameter("Kąt pola widzenia ptaków", 340f, 10f, 360f, 1f);
    public static final FloatParameter predatorFovRadius = new FloatParameter("Promienień pola widzenia drapieżników", 100f, 10f, 200f, 1f);
    public static final FloatParameter predatorFovAngleDeg = new FloatParameter("Kąt pola widzenia drapieżników", 90f, 10f, 360f, 1f);
    public static final BooleanParameter writeToFile = new BooleanParameter("Czy zapisać dane do pliku?", false);
    public static final BooleanParameter showFov = new BooleanParameter("Czy wyświetlić pola widzenia?", false);
    public static final BooleanParameter showVelocity = new BooleanParameter("Czy wyświetlić wektory prędkości?", false);
    public static final BooleanParameter antialiasing = new BooleanParameter("Czy wygładzić krawędzie?", true);

    private GuiParameters (){
    }

    public static class IntegerParameter {
        public final String name;
        private int value;
        public final int min;
        public final int max;
        public final int tick;
        public IntegerParameter(String name, int value, int min, int max, int tick) {
            this.name = name;
            this.value = value;
            this.min = min;
            this.max = max;
            this.tick = tick;
        }

        public IntegerParameter(int value) {
            this.name = "";
            this.value = value;
            this.min = Integer.MIN_VALUE;
            this.max = Integer.MAX_VALUE;
            this.tick = 1;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            if (value > max || value < min) {
                throw new IllegalArgumentException("Value is not between min and max");
            }
            this.value = value;
        }
    }

    public static class FloatParameter {
        public final String name;
        private float value;
        public final float min;
        public final float max;
        public final float tick;
        public FloatParameter(String name, float value, float min, float max, float tick) {
            this.name = name;
            this.value = value;
            this.min = min;
            this.max = max;
            this.tick = tick;
        }

        public FloatParameter(float value) {
            this.name = "";
            this.value = value;
            this.min = Float.MIN_VALUE;
            this.max = Float.MAX_VALUE;
            this.tick = Float.MIN_NORMAL;
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            if (value > max || value < min) {
                throw new IllegalArgumentException("Value is not between min and max");
            }
            this.value = value;
        }
    }

    public static class BooleanParameter {
        public final String name;
        private boolean value;

        public BooleanParameter(String name, boolean value) {
            this.name = name;
            this.value = value;
        }

        public boolean getValue() {
            return value;
        }

        public void setValue(boolean value) {
            this.value = value;
        }
    }
}
