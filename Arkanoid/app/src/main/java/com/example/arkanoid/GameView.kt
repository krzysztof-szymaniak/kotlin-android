package com.example.arkanoid

import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.random.Random

const val PLAYER_WIDTH = 300f
const val PLAYER_HEIGHT = 70f

const val BALL_SPEED = 30f
const val RADIUS = 20f
const val MAX_BOUNCE_ANGLE = 75 * Math.PI / 180

const val BRICK_HEIGHT = 70f

const val numRows = 6
const val numCols = 8

class Ball(
    var x: Float,
    var y: Float,
    var velX: Float,
    var velY: Float,
    val radius: Float,
    val color: Paint
) {
    fun draw(canvas: Canvas) {
        canvas.drawOval(x - radius, y - radius, x + radius, y + radius, color)
    }
}

class Brick(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val color: Paint,
    val border: Paint,
    var isHit: Boolean
) {
    fun draw(canvas: Canvas) {
        canvas.drawRect(x - width / 2, y - height / 2, x + width / 2, y + height / 2, color)
        canvas.drawRect(x - width / 2, y - height / 2, x + width / 2, y + height / 2, border)
    }
}

class Player(
    var x: Float,
    var y: Float,
    val width: Float,
    val height: Float,
    val color: Paint
) {
    fun draw(canvas: Canvas) {
        canvas.drawRect(x - width / 2, y - height / 2, x + width / 2, y + height / 2, color)
    }
}

class GameView(context: Context, attributeSet: AttributeSet) :
    SurfaceView(context, attributeSet), SurfaceHolder.Callback {
    private val thread: GameThread
    lateinit var parentActivity: MainActivity
    private val ball: Ball
    private val player: Player
    var bricks: MutableList<Brick>
    private var isBallFlying: Boolean
    private var dialogDisplayed = false


    init {
        holder.addCallback(this)
        thread = GameThread(holder, this)

        val rnd = Random.Default //kotlin.random
        val ballPaint = Paint().apply {
            color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        }
        ball = Ball(0f, 0f,0f, 0f, RADIUS, ballPaint)

        val playerPaint = Paint().apply {
            color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        }
        player = Player(0f, 0f, PLAYER_WIDTH, PLAYER_HEIGHT, playerPaint)
        bricks = mutableListOf()

        isBallFlying = false
        setOnLongClickListener {
            if (! isBallFlying)
                shootBall()
            true
        }
    }
    override fun surfaceCreated(holder: SurfaceHolder) {
        player.x = width / 2f
        player.y = 13f / 14 * height
        stickBallToBoard()
        if (parentActivity.bricks.isNotEmpty())
            bricks = parentActivity.bricks
        else
            generateBricks()

        thread.running = true
        thread.start()
    }

    private fun generateBricks(): MutableList<Brick> {
        val brickWidth = width / numCols.toFloat()
        val border = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 5f
            color = Color.BLACK
        }
        val colors = arrayListOf(
            Color.GRAY,
            Color.RED,
            Color.YELLOW,
            Color.BLUE,
            Color.MAGENTA,
            Color.GREEN
        )
        var cy = 1 / 8f * height
        for (i in 0 until numRows) {
            var cx = brickWidth / 2
            val color = Paint().apply { color = colors[i] }
            for (j in 0 until numCols) {
                bricks.add(Brick(cx, cy, brickWidth, BRICK_HEIGHT, color, border, false))
                cx += brickWidth
            }
            cy += BRICK_HEIGHT
        }
        return bricks
    }



    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        thread.running = false
        thread.join()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        if (canvas == null) return
        player.draw(canvas)
        ball.draw(canvas)
        for (b in bricks) {
            b.draw(canvas)
        }
    }

    fun update() {
        if (bricks.isEmpty() && !dialogDisplayed){
            dialogDisplayed = true
            parentActivity.runOnUiThread {
                val builder = AlertDialog.Builder(parentActivity)
                builder.setTitle("Game over!")
                builder.setMessage("Congratulations, you won!")
                builder.setPositiveButton("OK") { _, _ ->
                    parentActivity.finish()
                }
                builder.show()
            }

        }
        if (isBallFlying) {
            ball.x += ball.velX
            ball.y += ball.velY

            for (b in bricks) {
                checkCollisionWith(b)
            }
            bricks = bricks.filter { !it.isHit } as MutableList<Brick>
            checkCollisionWithPlayer()
            checkWallBounce()
        }

    }

    private fun checkWallBounce() {
        if (ball.x - ball.radius < 0 || ball.x + ball.radius > width) {
            ball.velX *= -1
        }

        if (ball.y - ball.radius < 0) {
            ball.velY *= -1
        }
        if (ball.y + ball.radius > height) {
            isBallFlying = false
            ball.velY = 0f
            ball.velX = 0f
            stickBallToBoard()
        }
    }

    private fun stickBallToBoard() {
        ball.x = player.x
        ball.y = player.y - ball.radius - player.height / 2
    }


    private fun checkCollisionWithPlayer() {
        val xDist = abs(ball.x - player.x) - player.width / 2
        val yDist = abs(ball.y - player.y) - player.height / 2
        if ((xDist < ball.radius && yDist < 0)
            || (yDist < ball.radius && xDist < 0)
            || (xDist.pow(2) + yDist.pow(2) < ball.radius.pow(2))
        ) {
            if (xDist < yDist) {
                // vertical collison
                bouncePlayer()
            } else {
                //horizontal colission
                ball.velX *= -1
            }
        }
    }

    private fun bouncePlayer() {
        val ratio = (player.x + player.width / 2 - ball.x) / (player.width / 2)
        val angle = ratio * MAX_BOUNCE_ANGLE
        ball.velX = (BALL_SPEED * cos(angle)).toFloat()
        ball.velY = (-BALL_SPEED * sin(angle)).toFloat()
    }

    private fun checkCollisionWith(brick: Brick) {
        val xDist = abs(ball.x - brick.x) - brick.width / 2
        val yDist = abs(ball.y - brick.y) - brick.height / 2
        if ((xDist < ball.radius && yDist < 0)
            || (yDist < ball.radius && xDist < 0)
            || (xDist.pow(2) + yDist.pow(2) < ball.radius.pow(2))
        ) {
            brick.isHit = true;
            if (xDist < yDist) {
                // vertical collison
                ball.velY *= -1;
            } else {
                //horizontal colission
                ball.velX *= -1;
            }
        }
    }

    private fun shootBall() {
        stickBallToBoard()
        isBallFlying = true
        val startingAngle = Math.random() * (135 - 45) + 45

        ball.velY = (-BALL_SPEED * sin(startingAngle * Math.PI / 180)).toFloat()
        ball.velX = (BALL_SPEED * cos(startingAngle * Math.PI / 180)).toFloat()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        player.x = event!!.x
        if(player.x + player.width/2 > width)
            player.x = width - player.width/2

        if(player.x - player.width/2 < 0)
            player.x = player.width/2

        if (! isBallFlying)
            stickBallToBoard()

        return true
    }
}