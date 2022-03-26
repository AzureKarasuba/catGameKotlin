package com.example.catGame

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class catGameKotlin : ApplicationAdapter(){
    lateinit var batch : SpriteBatch
    lateinit var background : Texture

    lateinit var cat : Texture
    var catX = 0f
    var catY = 0f

    var screenWidth = 0f
    var screenHeight = 0f
    var velocity = 0
    var gravity = 0

    var gameState = 0

    fun initialize(){
        velocity = 0
        gameState = 0
        gravity = 2

        catX = screenWidth/2 - cat.width/2
        catY = screenHeight/2

    }//initialize()

    override fun create() {
        super.create()

        screenHeight = Gdx.graphics.height.toFloat()
        screenWidth = Gdx.graphics.width.toFloat()

        batch = SpriteBatch()
        background = Texture("bg.jpg")

        cat = Texture("cat.png")

        initialize()

    }//create()

    override fun render() {
        super.render()
        batch.begin()

        batch.draw(background,0f,0f,screenWidth,screenHeight)

        if(gameState == 0){
            if(Gdx.input.justTouched()){
                gameState = 1
            }//if justTouched
        }//if gameState == 0

        if(gameState == 1){
            if(Gdx.input.justTouched()) {
                velocity = 30
            }
            velocity = velocity - gravity
            catY = catY + velocity

        }//if gameState == 1
        else if(gameState == 2){
            //when cat hits a bomb

        }

        if(gameState == 2){
            //draw game end state

        }else{
            batch.draw(cat,catX,catY)

            batch.end()
        }
    }//    override fun render() {

    override fun dispose() {
        super.dispose()
    }
}
