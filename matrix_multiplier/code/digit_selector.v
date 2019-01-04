`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Create Date: 10/26/2018 02:13:40 PM
// Design Name: 
// Module Name: digit_selector
// Project Name: Matrix Multiplier
// Description: Select which digit is currently being displayed and which will be
//              displayed next
// 
// Dependencies: 
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
//////////////////////////////////////////////////////////////////////////////////


module digit_selector(
    input clkd,
    input rst,
    output reg [3:0] digit_sel
    );
    reg [3:0] next_digit;

    //sequential logic to change digit_sel on the rising edge of the clock
    always@(posedge clkd, posedge rst)
    begin
        if(rst)
            digit_sel <= 4'b1110;
        else
            digit_sel <= next_digit;
    end
    
    //combinational logic to say what the next state of digit_sel will be
    always@(digit_sel)
    begin
        if(digit_sel == 4'b1110)      next_digit = 4'b1101;
        else if(digit_sel == 4'b1101) next_digit = 4'b1011;
        else if(digit_sel == 4'b1011) next_digit = 4'b0111;
        else                          next_digit = 4'b1110;
    end
    
endmodule
