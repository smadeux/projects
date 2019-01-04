`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Create Date: 10/25/2018 01:20:30 PM
// Design Name: 
// Module Name: sseg_x4 _top
// Project Name: Matrix Multiplier
// Description: Top level module for the seven segment display
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
//////////////////////////////////////////////////////////////////////////////////


module sseg_x4_top(
    input clk,
    input btnC,
    input [15:0] sw,
    output [6:0] seg,
    output [3:0] an,
    output dp,
    output clkd
    );
    
    wire [3:0] not_used;
    wire [3:0] hex_num;
    
    //clock divider module
    clk_gen divider(.clk(clk), .rst(btnC), .clk_div(clkd));

    //choose which digit will be displayed
    digit_selector selector(.clkd(clkd), .rst(btnC), .digit_sel(an));

    //match the inputs to the digit that is being displayed
    hex_num_gen generator(.digit_sel(an), .sw(sw), .hex_num(hex_num));
    
    //display a digit based on a four-bit input
    seven_seg an1(.sw(hex_num), .seg(seg), .dp(dp), .an(not_used));
    
endmodule
