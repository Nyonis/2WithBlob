package helper;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

public class MapConvertHelper {

    public final static float WORLD_FAC = .1f;
    public final static float WORLD_SCALE = 10;

    public static Array<Body> mapToCollisionBody(Map map, String layerName, World world, Object uData) {
        MapObjects objects = map.getLayers().get(layerName).getObjects();

        Array<Body> bodies = new Array<Body>();

        for(MapObject object : objects) {

            if (object instanceof TextureMapObject) {
                continue;
            }

            Shape shape;

            if (object instanceof RectangleMapObject) {
                shape = getRectangle((RectangleMapObject)object);
            } else if (object instanceof PolygonMapObject) {
                shape = getPolygon((PolygonMapObject)object);
            } else if (object instanceof PolylineMapObject) {
                shape = getPolyline((PolylineMapObject)object);
            } else if (object instanceof CircleMapObject) {
                shape = getCircle((CircleMapObject)object);
            } else {
                continue;
            }

            BodyDef bd = new BodyDef();
            bd.type = BodyType.StaticBody;
            Body body = world.createBody(bd);
            FixtureDef fDef = new FixtureDef();
            fDef.shape = shape;
            fDef.filter.groupIndex = 1;
            fDef.density = 1;
            body.createFixture(fDef);

            if(uData != null)
                body.setUserData(uData);

            bodies.add(body);

            shape.dispose();
        }
        return bodies;
    }

    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) * WORLD_FAC,
                                   (rectangle.y + rectangle.height * 0.5f ) * WORLD_FAC);
        polygon.setAsBox(rectangle.width * 0.5f * WORLD_FAC,
                         rectangle.height * 0.5f * WORLD_FAC,
                         size,
                         0.0f);
        return polygon;
    }

    private static CircleShape getCircle(CircleMapObject circleObject) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius * WORLD_FAC);
        circleShape.setPosition(new Vector2(circle.x , circle.y ));
        return circleShape;
    }

    private static PolygonShape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = vertices[i] ;
        }

        polygon.set(worldVertices);
        return polygon;
    }

    private static ChainShape getPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] ;
            worldVertices[i].y = vertices[i * 2 + 1] ;
        }

        ChainShape chain = new ChainShape(); 
        chain.createChain(worldVertices);
        return chain;
    }
}