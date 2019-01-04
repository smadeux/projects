`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Create Date: 11/01/2016 01:36:54 PM
// Design Name: 
// Module Name: debounce_div
// Project Name: Matrix Multiplier
// Description: Divides the clock for the button debouncing module
// 
// Dependencies: 
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
//////////////////////////////////////////////////////////////////////////////////


module debounce_div(
    input clk,
    output clk_deb
    );
    reg [15:0] cnt;
    //reg cnt;
    
    assign clk_deb = cnt[15];
    //assign clk_deb = cnt;
    
    initial cnt = 0;
    always @(posedge clk)
        cnt <= cnt + 1;
        
endmodule
