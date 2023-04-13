package com.example.calculatorapp

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.calculatorapp.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var lastNumeric = false
    var stateError = false
    var lastDot = false
    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onDigitClick(view: View) {
        if(stateError)
        {
            binding.tv1.text = (view as Button).text
            stateError = false
        }
        else
        {
            binding.tv1.append((view as Button).text)
        }
        lastNumeric = true
        onEqual()
    }
    fun onBackClick(view: View) {
        binding.tv1.text = binding.tv1.toString().dropLast(1)
        try{
            val lastChar = binding.tv1.text.toString().last()
            if (lastChar.isDigit())
            {
                onEqual()
            }
        }
        catch (e:Exception){
            binding.tv2.text=""
            binding.tv2.visibility=View.GONE
            Log.e("last char error",e.toString())
        }
    }
    fun onClearClick(view: View) {

        binding.tv1.text = ""
        lastNumeric = false

    }
    fun onOperatorClick(view: View) {

        if(!stateError && lastNumeric){
            binding.tv1.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            onEqual()
        }


    }
    fun onAllClearClick(view: View)
    {
        binding.tv1.text=""
        binding.tv2.text=""
        stateError=false
        lastDot=false
        lastNumeric=false
        binding.tv2.visibility = View.GONE
    }
    fun onEqualClick(view: View) {

        onEqual()
        binding.tv1.text = binding.tv2.text.toString().drop(1)

    }
    fun onEqual(){
        if(lastNumeric && !stateError){
            val txt = binding.tv1.text.toString()
            expression = ExpressionBuilder(txt).build()
            try
            {
                val result  = expression.evaluate()
                binding.tv2.visibility = View.VISIBLE
                binding.tv2.text = "=" + result.toString()
            }
            catch (ex : ArithmeticException)
            {
                Log.e("evaluate error",ex.toString())
                binding.tv2.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }
}