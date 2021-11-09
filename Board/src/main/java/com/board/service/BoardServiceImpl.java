package com.board.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.board.domain.BoardDTO;
import com.board.mapper.BoardMapper;
import com.board.paging.Criteria;
import com.board.paging.PaginationInfo;

// 비지니스 로직 담당
@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper boardMapper;
	
	// 게시글 생성 및 수정 처리
	@Override
	public boolean registerBoard(BoardDTO params) {
		int queryResult = 0;
		
		// 게시글 생성
		if (params.getIdx() ==null) {
			queryResult = boardMapper.insertBoard(params);
		}
		// 게시글 수정
		else {
			queryResult = boardMapper.updateBoard(params);
		}
		
		// 쿼리 결과
		return (queryResult == 1) ? true : false;
	}
	
	// 하나의 게시글 조회
	@Override
	public BoardDTO getBoardDetail(long idx) {
		return boardMapper.selectBoardDetail(idx);
	}
	
	// 게시글 조회 후 삭제 처리
	@Override
	public boolean deleteBoard(long idx) {
		int queryResult =0;
		
		BoardDTO board = boardMapper.selectBoardDetail(idx);
		
		// 게시글이 없고 delete_yn이 n이면 삭제
		if (board != null && "N".equals(board.getDeleteYn())) {
			queryResult = boardMapper.deleteBoard(idx);
		}
		
		return (queryResult ==1) ? true : false;
	}
	
	// 삭제되지 않은 전체 게시글 조회
	@Override
	public List<BoardDTO> getBoardList(BoardDTO params){
		List<BoardDTO> boardList = Collections.emptyList();
		
		int boardTotalCount = boardMapper.selectBoardTotalCount(params);

		PaginationInfo paginationInfo = new PaginationInfo(params);
		paginationInfo.setTotalRecordCount(boardTotalCount);
		
		params.setPaginationInfo(paginationInfo);
		
		if (boardTotalCount > 0) {
			boardList = boardMapper.selectBoardList(params);
		}
		
		return boardList;
	}

}
