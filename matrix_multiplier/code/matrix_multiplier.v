`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Create Date: 12/18/2018 02:53:14 PM
// Design Name: 
// Module Name: matrix_multiplier
// Project Name: Matrix Multiplier
// Description: Matrix multiplier top level module
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
//////////////////////////////////////////////////////////////////////////////////


module matrix_multiplier(
    input clk,
    input [3:0] matPos,
    input origMat,
    input modMat,
    input scaleSel,
    input transXSel,
    input transYSel,
    input transZSel,
    input btnU,
    input btnD,
    input btnC,
    input btnR,
    input btnL,
    output [3:0] an,
    output dp,
    output [6:0] seg
    );
    
    wire clk_deb;
    
    //The five debounced buttons
    wire numUp;
    wire numDown;
    wire bMultiply;
    wire bRst;
    wire bExecute;
    
    //Button presses delayed by 1 clock cycle
    reg numUp_tm1;
    reg numDown_tm1;
    
    //Singal to increment or decrement what is being viewed
    reg inc_num;
    reg dec_num;
    
    //Singal to do matrix multiplication
    reg multiply_mat;
    
    //The 16 bit number currently being viewd
    reg [15:0] current_num_view;
    
    //Which entry in the matrix
    reg [4:0] addr;
    
    //Matricies
    reg [15:0] origin_mat3x3 [8:0];
    reg [15:0] mod_mat3x3 [8:0];
    reg [15:0] trans_mat4x4 [15:0];
    
    //Set the debounce clock and debounce the buttons
    debounce_div div1(.clk(clk), .clk_deb(clk_deb));
    btn_debounce up(.clk(clk_deb), .btn_in(btnU), .btn_status(numUp));
    btn_debounce down(.clk(clk_deb), .btn_in(btnD), .btn_status(numDown));
    btn_debounce left(.clk(clk_deb), .btn_in(btnL), .btn_status(bMultiply));
    //Add reset button incase I need it in the future
    btn_debounce right(.clk(clk_deb), .btn_in(btnR), .btn_status(bRst));
    btn_debounce center(.clk(clk_deb), .btn_in(btnC), .btn_status(bExecute));
    
    //Send the number currently being vied to the Binary to BCD converter to be displayed
    //on the seven segment display
    bin2BCD_4dig diplay_num(.clk(clk), .sw(current_num_view), .seg(seg), .dp(dp), .an(an));
    
    //Assign first four switches to addr
    always@(matPos)
    begin
        if(matPos < 4'h9) addr = matPos;
        else addr = 4'h8;
    end
    
    //Choose which number goes to the seven segment display
    always@(*)
    begin
        if(origMat) current_num_view = origin_mat3x3[addr];
        else if(modMat) current_num_view = mod_mat3x3[addr];
        else if(transZSel) current_num_view = trans_mat4x4[4'hb];
        else if(transYSel) current_num_view = trans_mat4x4[4'h7];
        else if(transXSel) current_num_view = trans_mat4x4[4'h3];
        else if(scaleSel) current_num_view = trans_mat4x4[addr];
        else current_num_view = 16'h0000;
    end
    
    //Increment signal delayed by 1 cycle
    always@(posedge clk)
    begin
        numUp_tm1 <= numUp;
        numDown_tm1 <= numDown;
    end
    
    //What is happening based on button presses
    always@(*)
    begin
        multiply_mat = bMultiply && bExecute;
        inc_num = numUp && !numUp_tm1;
        dec_num = numDown && !numDown_tm1;
    end
    
    //Procedures for each button press function
    always@(posedge clk)
    begin
        if(inc_num) 
        begin
            //Increment what is being viewed
            if(origMat) origin_mat3x3[addr] = origin_mat3x3[addr] + 1;
            else if(transZSel) trans_mat4x4[4'hb] = trans_mat4x4[4'hb] + 1;
            else if(transYSel) trans_mat4x4[4'h7] = trans_mat4x4[4'h7] + 1;
            else if(transXSel) trans_mat4x4[4'h3] = trans_mat4x4[4'h3] + 1;
            else if(scaleSel) 
            begin
                trans_mat4x4[4'h0] = trans_mat4x4[4'h0] + 1; 
                trans_mat4x4[4'h5] = trans_mat4x4[4'h5] + 1;
                trans_mat4x4[4'ha] = trans_mat4x4[4'ha] + 1;
            end
        end
        else if(dec_num && current_num_view > 16'h0000)
        begin
            //Decrement what is being viewed if it is greater than 0
            if(origMat) origin_mat3x3[addr] = origin_mat3x3[addr] - 1;
            else if(transZSel) trans_mat4x4[4'hb] = trans_mat4x4[4'hb] - 1;
            else if(transYSel) trans_mat4x4[4'h7] = trans_mat4x4[4'h7] - 1;
            else if(transXSel) trans_mat4x4[4'h3] = trans_mat4x4[4'h3] - 1;
            else if(scaleSel && current_num_view > 16'h0001) 
            begin
                trans_mat4x4[4'h0] = trans_mat4x4[4'h0] - 1; 
                trans_mat4x4[4'h5] = trans_mat4x4[4'h5] - 1; 
                trans_mat4x4[4'ha] = trans_mat4x4[4'ha] - 1;
            end
        end
        else if(multiply_mat)
        begin
            //Steps for multiplying matricies in parallel
            mod_mat3x3[4'h0] <= trans_mat4x4[4'h0]*origin_mat3x3[4'h0] + trans_mat4x4[4'h3];
            mod_mat3x3[4'h1] <= trans_mat4x4[4'h0]*origin_mat3x3[4'h1] + trans_mat4x4[4'h3];
            mod_mat3x3[4'h2] <= trans_mat4x4[4'h0]*origin_mat3x3[4'h2] + trans_mat4x4[4'h3];
            mod_mat3x3[4'h3] <= trans_mat4x4[4'h5]*origin_mat3x3[4'h3] + trans_mat4x4[4'h7];
            mod_mat3x3[4'h4] <= trans_mat4x4[4'h5]*origin_mat3x3[4'h4] + trans_mat4x4[4'h7];
            mod_mat3x3[4'h5] <= trans_mat4x4[4'h5]*origin_mat3x3[4'h5] + trans_mat4x4[4'h7];
            mod_mat3x3[4'h6] <= trans_mat4x4[4'ha]*origin_mat3x3[4'h6] + trans_mat4x4[4'hb];
            mod_mat3x3[4'h7] <= trans_mat4x4[4'ha]*origin_mat3x3[4'h7] + trans_mat4x4[4'hb];
            mod_mat3x3[4'h8] <= trans_mat4x4[4'ha]*origin_mat3x3[4'h8] + trans_mat4x4[4'hb];
        end
    end
    
endmodule
