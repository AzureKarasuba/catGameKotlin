package com.example.catGame

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Intersector

import com.badlogic.gdx.math.Rectangle
import java.util.*
import kotlin.collections.HashMap

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class catGameKotlin : ApplicationAdapter(){
    lateinit var batch : SpriteBatch
    lateinit var background : Texture

    lateinit var cat : Texture
    var catX = 0f
    var catY = 0f
    var catWidth = 0f
    lateinit var sleepy: Texture

    var screenWidth = 0f
    var screenHeight = 0f
    var velocity = 0f
    var gravity = 0.2f

    var w = 0f
    var h = 0f

    //food
    var fishXs = HashMap<Int, Float>()
    var fishYs = HashMap<Int, Float>()
    var fishRectangles = HashMap<Int, Rectangle>()

    //poison
    var poisonXs = HashMap<Int, Float>()
    var poisonYs = HashMap<Int, Float>()
    var poisonRectangles = HashMap<Int, Rectangle>()
    lateinit var poison: Texture

    lateinit var font: BitmapFont

    var poisonCount = 0
    var numOfPoison = 0
    lateinit var fish: Texture
    var fishCount = 0
    lateinit var random: Random
    var numOfFish = 0
    lateinit var catRectangle : Rectangle

    var score = 0
    var gameState = 0

    fun initialize(){
        velocity = 0f
        gameState = 0
        gravity = 2f

        catX = screenWidth/4 - cat.width/4
        catY = screenHeight/4
        w = 0.5f * screenWidth
        h = 0.5f * screenHeight
        catWidth = cat.width.toFloat()

        poison = Texture("poison.png")
        fish = Texture("fish.png")
        random = Random(1)
        font = BitmapFont()
        font.color = Color.BLACK
        font.data.setScale(10f)

    }//initialize()

    override fun create() {
        super.create()

        screenHeight = Gdx.graphics.height.toFloat()
        screenWidth = Gdx.graphics.width.toFloat()

        batch = SpriteBatch()
        background = Texture("bg.jpg")

        cat = Texture("cat.png")
        sleepy = Texture("sleeping.png")

        initialize()

    }//create()

    fun makeFish() {
        val height: Float = random.nextFloat() * Gdx.graphics.height
        fishXs[numOfFish] = (Gdx.graphics.width).toFloat()
        fishYs[numOfFish] = height
        numOfFish += 1

    }//makeFish

    fun makePoison() {
        val height = random.nextFloat() * Gdx.graphics.height
        poisonYs[numOfPoison] = height
        poisonXs[numOfPoison] = (Gdx.graphics.width).toFloat()
        numOfPoison += 1
    }

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

            //POISON
            if (poisonCount < 250) {
                poisonCount++
            } else {
                poisonCount = 0
                makePoison()
            }

            for (i in 0 until numOfPoison) {
                batch.draw(poison, poisonXs[i]!!, poisonYs[i]!!)
                poisonXs[i] = poisonXs[i]!! - 6
                poisonRectangles[i] =
                    Rectangle(
                        poisonXs[i]!!.toFloat(), poisonYs[i]!!.toFloat(),
                        poison.width.toFloat(), poison.height.toFloat()
                    )
            }//for

            //FISH
            if (fishCount < 100) {
                fishCount += 1
            } else  {
                fishCount = 0
                makeFish()
            }//if

            for (i in 0 until numOfFish) {
                batch.draw(fish, fishXs[i]!!, fishYs[i]!!)
                fishXs[i] = fishXs[i]!! - 8
                fishRectangles[i] =
                    Rectangle(
                        fishXs[i]!!.toFloat(), fishYs[i]!!.toFloat(),
                        fish.width.toFloat(), fish.height.toFloat()
                    )
            }//for

            //mouse clicked
            if(Gdx.input.justTouched()) {
                velocity = 30f
            }//if
            velocity = velocity - gravity
            catY = catY + velocity

        }//if gameState == 1
        else if(gameState == 2){
            //when cat hits a bomb
            if (Gdx.input.justTouched()) {
                gameState = 1
                catY = screenHeight
                score = 0
                velocity = 0f
                fishCount = 0
                poisonCount = 0
                numOfFish = 0
                numOfPoison = 0
            }//if (Gdx.input.justTouched()
        }//else if (gameState == 2)

        if(gameState == 2){
            //draw game end state
            batch.draw(sleepy, w-catWidth/2, catY / 2 -100)
        }else{
            batch.draw(cat,catX,catY)
//            batch.end()
        }
        catRectangle = Rectangle(
            w - catWidth / 2.toFloat(), catY / 2 - 100.toFloat(),
            cat.width.toFloat(), cat.height.toFloat()
        )

        for (i in 0 until numOfFish) {
            if (Intersector.overlaps(catRectangle, fishRectangles[i])) {
                score++
                fishXs[i] = -100f
                fishYs[i] = -100f
                break
            }//if Intersector.overlaps
        }

        for (i in 0 until numOfPoison) {
            if (Intersector.overlaps(catRectangle,poisonRectangles[i])) {
                poisonXs[i] = -100f
                poisonYs[i] = -100f
                gameState = 2
            }
        }//for i in 0 until numOfPoison

        font.draw(batch, score.toString(), 100f, 150f)
        batch.end()

    }//render



    override fun dispose() {
        super.dispose()
        batch.dispose()
    }//dispose
}//catGameKotlin
