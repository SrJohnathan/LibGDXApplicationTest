package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.io.File;

import particles.CameraView;
import particles.EffekseerManager;
import particles.OnAnimationComplete;
import particles.ParticleEffekseer;


public class MyGdxGame extends ApplicationAdapter implements InputProcessor {
    SpriteBatch batch;
    Texture img;
    private ParticleEffekseer effekseer;

    private EffekseerManager manager;
   CameraInputController controller;
    public ModelBatch modelBatch;
    public Model model;
    public ModelInstance instance;
    PerspectiveCamera perspectiveCamera ;
    OrthographicCamera orthographicCamera;
    FitViewport viewport;

    public Environment environment;


    AssetManager assetManager = new AssetManager();
    boolean loadObjs = false;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        modelBatch = new ModelBatch();

         orthographicCamera = new OrthographicCamera(1280f,720f);
        perspectiveCamera = new PerspectiveCamera(67, 1280f, 720);

        viewport= new FitViewport(1280,720,orthographicCamera);

        assetManager.load("data/obj/duelo.g3dj",Model.class);







        EffekseerManager.InitializeEffekseer();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        perspectiveCamera.lookAt(0,0,0);
        perspectiveCamera.near = 1f;
        perspectiveCamera.far = 300f;
        perspectiveCamera.up.set(0f,1f,0f);
        perspectiveCamera.update();

        controller = new CameraInputController(orthographicCamera);

        manager = new EffekseerManager(orthographicCamera);
        manager.setViewport(viewport);





        effekseer = new ParticleEffekseer(manager);
        effekseer.setMagnification(20f);
        try {
            effekseer.load("data/tu.efk",false);
        } catch (Exception e) {
            e.printStackTrace();
        }





     //  effekseer.setLacation(0, 2f, 0f);





     //   instance.transform.translate(0,0,150);



        System.out.println(Gdx.app.getGraphics().getGLVersion().getMajorVersion() + "." + Gdx.app.getGraphics().getGLVersion().getMinorVersion());

       // perspectiveCamera.position.set(-0.05873839f, 1.5226717f, 6.1832256f);

    }

    @Override
    public void render() {


        Gdx.input.setInputProcessor( new InputMultiplexer( this,controller));

        Gdx.gl.glClearColor(0f, 0f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);




        modelBatch.begin(perspectiveCamera);
        if(instance  != null){
            modelBatch.render(instance,environment);
        }
        modelBatch.end();


        batch.begin();
        batch.draw(img,0,0);
        batch.end();


        manager.draw(Gdx.graphics.getDeltaTime());




        if(!loadObjs) {
            assetManager.update();

            if (assetManager.isFinished()) {
                loadObjs();
                loadObjs = true;


            }
        }

    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        modelBatch.dispose();
        model.dispose();
        manager.dispose();

    }

    @Override
    public void resize(int width, int height) {
        orthographicCamera.update();
        perspectiveCamera.update();
        viewport.update(width, height);
    }

    public void loadObjs(){

        model = assetManager.get("data/obj/duelo.g3dj",Model.class);

        instance = new ModelInstance(model);


        instance.transform.rotate(Vector3.Y,90);
        Vector3 vector3 = new Vector3();
        vector3 =  instance.transform.getTranslation(vector3);
        instance.transform.translate(vector3.x,vector3.y -2,0f);
        effekseer.setLacation( 50,  0,2.7f);

        perspectiveCamera.position.set(-0.05873839f, 1.5226717f, 8.1832256f);



        effekseer.setOnAnimationComplete(new OnAnimationComplete() {
            @Override
            public void finish() {
                System.out.println("oiiiii");
            }
        });

    }


    @Override
    public boolean keyDown(int keycode) {


        if(keycode == Input.Keys.E){

           instance.transform.rotate(Vector3.Y,90);

           Vector3 vector3 = new Vector3();
              vector3 =  instance.transform.getTranslation(vector3);

            instance.transform.translate(vector3.x,vector3.y -1,vector3.z);
          //  manager.setCameraPosition();
        }

        if(keycode == Input.Keys.W){

            effekseer.setLacation(0f, -0.5f,   3.1832256f);

        }


        if(keycode == Input.Keys.S){


            effekseer.setLacation(effekseer.getX() ,effekseer.getY()  - (2* Gdx.graphics.getDeltaTime()),effekseer.getZ() );

        }

        if(keycode == Input.Keys.H){


            effekseer.play();


        }


        if(keycode == Input.Keys.A){

            effekseer.resume();

        }

        if(keycode == Input.Keys.P){

            effekseer.pause();

        }

        if(keycode == Input.Keys.Z){


            orthographicCamera.position.set(
                    orthographicCamera.position.x,
                    (orthographicCamera.position.y += (50 * Gdx.graphics.getDeltaTime())),
                    0);


        }

        if(keycode == Input.Keys.T){

            perspectiveCamera.position.set(
                    perspectiveCamera.position.x,
                    (perspectiveCamera.position.y += Gdx.graphics.getDeltaTime()),
                    perspectiveCamera.position.z);

        }



        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
