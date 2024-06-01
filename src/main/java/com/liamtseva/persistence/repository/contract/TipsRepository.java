package com.liamtseva.persistence.repository.contract;

import com.liamtseva.persistence.entity.Tips;
import java.util.List;

public interface TipsRepository {
  List<Tips> getAllTips();
}