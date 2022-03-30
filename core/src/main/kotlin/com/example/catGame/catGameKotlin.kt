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
import java.util.concurrent.RecursiveAction
import kotlin.random.Random
import kotlin.*

import kotlin.collections.HashMap
import kotlin.concurrent.schedule
import kotlin.concurrent.timerTask
import java.util.logging.Handler as Handler1

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class catGameKotlin : ApplicationAdapter(){
    lateinit var batch : SpriteBatch
    lateinit var background : Texture

    lateinit var cat : Array<Texture>
    var catX = 0f
    var catY = 0f
    var catWidth = 0f
    lateinit var sleepy: Texture
    var catState = 0
    var pause = 0

    var screenWidth = 0f
    var screenHeight = 0f
    var velocity = 0f
    var gravity = 2f

    var w = 0f
    var h = 0f

    //food
    var fishXs = HashMap<Int, Float>()
    var fishYs = HashMap<Int, Float>()
    var fishRectangles = HashMap<Int, Rectangle>()

    var milkXs = HashMap<Int, Float>()
    var milkYs = HashMap<Int, Float>()
    var milkRectangles = HashMap<Int, Rectangle>()
    lateinit var milk: Texture
    var milkCount = 0
    var numOfMilks = 0

    var heartXs = HashMap<Int, Float>()
    var heartYs = HashMap<Int, Float>()
    var heartRectangles = HashMap<Int, Rectangle>()
    lateinit var heart: Texture
    var heartCount = 0
    var numOfHearts = 0

    //poison
    var poisonXs = HashMap<Int, Float>()
    var poisonYs = HashMap<Int, Float>()
    var poisonRectangles = HashMap<Int, Rectangle>()
    lateinit var poison: Texture

    lateinit var font: BitmapFont
    lateinit var message: BitmapFont

    lateinit var highest : BitmapFont

    var poisonCount = 0
    var numOfPoison = 0
    lateinit var fish: Texture
    var fishCount = 0
    lateinit var random: Random
    var numOfFish = 0
    lateinit var catRectangle : Rectangle

    var score = 0
    var highestScore = 0
    var gameState = 0

    fun initialize(){
        velocity = 0f
        gameState = 0
        gravity = 0.2f
        catX = screenWidth/2 - cat[0].width/2
        catY = screenHeight/2- cat[0].height/2
        catWidth = cat[0].width.toFloat()

        random = Random(1)
        font = BitmapFont()
        font.color = Color.BLACK
        font.data.setScale(10f)

        highest = BitmapFont()
        highest.color = Color.BLACK
        highest.data.setScale(10f)


        message = BitmapFont()
        message.color = Color.ORANGE
        message.data.setScale(5f)


    }//initialize()

    override fun create() {
        super.create()
        batch = SpriteBatch()
        screenHeight = Gdx.graphics.height.toFloat()
        screenWidth = Gdx.graphics.width.toFloat()
        w = 0.5f * screenWidth
        h = 0.5f * screenHeight
        background = Texture("background.jpeg")
        poison = Texture("poison.png")
        fish = Texture("fish.png")
        cat = arrayOf(
            Texture("frame1.png"), Texture("frame2.png"),
            Texture("frame3.png")
        )
        milk = Texture("milk.png")
        heart  = Texture("heart.png")
        sleepy = Texture("lose.png")

        initialize()

    }//create()

    fun makeFish() {
        val height: Float = random.nextFloat() * Gdx.graphics.height
        fishXs[numOfFish] = (Gdx.graphics.width).toFloat()
        fishYs[numOfFish] = height
        numOfFish += 1

    }//makeFish

    fun makeMilk() {
        val height: Float = random.nextFloat() * Gdx.graphics.height
        milkXs[numOfMilks] = (Gdx.graphics.width).toFloat()
        milkYs[numOfMilks] = height
        numOfMilks += 1
    }//makeMilk

    fun  makeHearts() {
        val height: Float = random.nextFloat() * Gdx.graphics.height
        heartXs[numOfHearts] = (Gdx.graphics.width).toFloat()
        heartYs[numOfHearts] = height
        numOfHearts += 1
    }

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
                poisonXs[i] = poisonXs[i]!! - 8
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
                fishXs[i] = fishXs[i]!! - 4
                fishRectangles[i] =
                    Rectangle(
                        fishXs[i]!!.toFloat(), fishYs[i]!!.toFloat(),
                        fish.width.toFloat(), fish.height.toFloat()
                    )
            }//for

            if (milkCount < 100) {
                milkCount += 1
            } else {
                milkCount = 0
                makeMilk()
            }

            for (i in 0 until numOfMilks) {
                batch.draw(milk, milkXs[i]!!, milkYs[i]!!)
                milkXs[i] = milkXs[i]!! - 4
                milkRectangles[i] =
                    Rectangle(
                        milkXs[i]!!.toFloat(), milkYs[i]!!.toFloat(),
                        milk.width.toFloat(), milk.height.toFloat()
                    )
            }//for

            if (heartCount < 100) {
                heartCount += 1
            } else {
                heartCount = 0
                makeHearts()
            }

            for (i in 0 until numOfHearts) {
                batch.draw(heart, heartXs[i]!!, heartYs[i]!!)
                heartXs[i] = heartXs[i]!! - 4
                heartRectangles[i] =
                    Rectangle(
                        heartXs[i]!!.toFloat(), heartYs[i]!!.toFloat(),
                        heart.width.toFloat(), heart.height.toFloat()
                    )
            }//for



            //mouse clicked
            if(Gdx.input.justTouched()) {
                velocity -= 10
            }//if

            if (pause < 6) {
                pause += 1
            }else {
                pause = 0
                catState = (catState + 1) % 3
            }
            velocity += gravity
            catY -= velocity

            if (catY < 160) {
                catY = 160f
            }

//            velocity = velocity - gravity
//            catY = catY + velocity

        }//if gameState == 1
        else if(gameState == 2){
            //when cat hits a bomb
                if(highestScore < score){
                    highestScore = score
                }
            if (Gdx.input.justTouched()) {
                gameState = 1
                catY = screenHeight
                score = 0
                velocity = 0f
                fishCount = 0
                poisonCount = 0
                numOfFish = 0
                numOfPoison = 0
                heartCount = 0
                milkCount = 0
                numOfHearts = 0
                numOfMilks = 0
            }//if (Gdx.input.justTouched()
        }//else if (gameState == 2)

        if(gameState == 2){
            //draw game end state
            batch.draw(sleepy, w-catWidth/2, catY / 2 -100)
        }else{
            batch.draw(cat[catState],catX,catY)
//            batch.end()
        }
        catRectangle = Rectangle(
            catX.toFloat(), catY.toFloat(),
            cat[catState].width.toFloat(), cat[catState].height.toFloat()
        )

        for (i in 0 until numOfFish) {
            if (Intersector.overlaps(catRectangle, fishRectangles[i])) {
//                message.draw(batch, "Fish: + 3", catX, catY)
                score += 3
                fishXs[i] = -100f
                fishYs[i] = -100f
                break
            }//if Intersector.overlaps
        }

        for (i in 0 until numOfHearts) {
            if (Intersector.overlaps(catRectangle, heartRectangles[i])) {
//                message.draw(batch, "Heart: + 1", catX, catY)
                heartXs[i] = -100f
                heartYs[i] = -100f
                score++
                break
            }//if Intersector.overlaps
        }

        for (i in 0 until numOfMilks) {
            if (Intersector.overlaps(catRectangle, milkRectangles[i])) {
//                message.draw(batch, "Milk: + 2", catX, catY)
                score += 2
                milkXs[i] = -100f
                milkYs[i] = -100f
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

        message.draw(batch, "Milk: + 2 \nHeart: + 1 \nFish: + 3 ", 750f, 1700f)
        font.draw(batch, score.toString(), 100f, 150f)
        font.draw(batch, "Record: " + highestScore.toString(), 700f, 150f)
        batch.end()

    }//render
    override fun dispose() {
        super.dispose()
        batch.dispose()
    }//dispose
}//catGameKotlin
